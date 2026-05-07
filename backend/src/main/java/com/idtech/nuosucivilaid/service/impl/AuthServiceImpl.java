package com.idtech.nuosucivilaid.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.idtech.nuosucivilaid.dto.LoginRequest;
import com.idtech.nuosucivilaid.dto.RegisterRequest;
import com.idtech.nuosucivilaid.entity.SysRole;
import com.idtech.nuosucivilaid.entity.SysUser;
import com.idtech.nuosucivilaid.exception.BusinessException;
import com.idtech.nuosucivilaid.repository.SysRoleRepository;
import com.idtech.nuosucivilaid.repository.SysUserRepository;
import com.idtech.nuosucivilaid.service.AuthService;
import com.idtech.nuosucivilaid.util.CaptchaManager;
import com.idtech.nuosucivilaid.util.JwtUtil;
import com.idtech.nuosucivilaid.vo.LoginResponse;
import com.idtech.nuosucivilaid.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CaptchaManager captchaManager;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("用户登录业务处理，用户名: {}", request.getUsername());

        // 1. 验证验证码
        if (!captchaManager.verifyCaptcha(request.getCaptchaId(), request.getCaptcha())) {
            log.warn("用户登录验证码校验失败，用户名: {}", request.getUsername());
            throw BusinessException.loginCaptchaError();
        }

        // 2. 查询用户
        SysUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(BusinessException::loginAuthFailed);

        // 3. 校验密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("用户登录密码错误，用户名: {}", request.getUsername());
            throw BusinessException.loginAuthFailed();
        }

        // 4. 校验账号状态
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            log.warn("用户登录账号已被禁用，用户名: {}", request.getUsername());
            throw BusinessException.loginForbidden();
        }

        // 5. 查询角色
        SysRole role = roleRepository.findById(user.getRoleId())
                .orElseThrow(BusinessException::loginRoleNotFound);

        // 6. 生成 Token
        boolean rememberMe = Boolean.TRUE.equals(request.getRememberMe());
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), role.getRoleCode(), rememberMe);

        // 7. 组装用户信息
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .roleName(role.getRoleName())
                .roleCode(role.getRoleCode())
                .build();

        Long expiresIn = rememberMe ? jwtUtil.getRememberMeExpiration() : jwtUtil.getExpiration();

        log.info("用户登录成功，用户名: {}, 角色: {}", user.getUsername(), role.getRoleName());
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .userInfo(userInfo)
                .build();
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        log.info("用户注册业务处理，用户名: {}", request.getUsername());

        // 1. 验证验证码
        if (!captchaManager.verifyCaptcha(request.getCaptchaId(), request.getCaptcha())) {
            log.warn("用户注册验证码校验失败，用户名: {}", request.getUsername());
            throw BusinessException.loginCaptchaError();
        }

        // 2. 校验密码一致性
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn("用户注册密码不一致，用户名: {}", request.getUsername());
            throw BusinessException.registerPasswordNotMatch();
        }

        // 3. 校验用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("用户注册用户名已存在，用户名: {}", request.getUsername());
            throw BusinessException.registerUserExist();
        }

        // 4. 校验手机号（若填写）
        if (StrUtil.isNotBlank(request.getMobile())) {
            if (!Validator.isMobile(request.getMobile())) {
                log.warn("用户注册手机号格式错误，用户名: {}", request.getUsername());
                throw BusinessException.registerPhoneFormatError();
            }
            if (userRepository.existsByMobile(request.getMobile())) {
                log.warn("用户注册手机号已存在，手机号: {}", request.getMobile());
                throw BusinessException.registerPhoneExist();
            }
        }

        // 5. 获取默认角色
        SysRole defaultRole = roleRepository.findByRoleCode("user")
                .orElseThrow(BusinessException::registerUserRoleNotFound);

        // 6. 创建用户
        SysUser user = SysUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile())
                .roleId(defaultRole.getId())
                .createTime(Instant.now())
                .updateTime(Instant.now())
                .isDeleted(false)
                .build();

        userRepository.save(user);
        log.info("用户注册成功，用户名: {}, 用户ID: {}", user.getUsername(), user.getId());
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("获取当前用户信息失败，用户未认证");
            throw BusinessException.authFailed();
        }

        String username = authentication.getName();
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(BusinessException::notFound);

        SysRole role = roleRepository.findById(user.getRoleId())
                .orElseThrow(BusinessException::notFound);

        log.debug("获取当前用户信息成功，用户名: {}, 角色: {}", username, role.getRoleName());
        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .roleName(role.getRoleName())
                .roleCode(role.getRoleCode())
                .build();
    }
}
