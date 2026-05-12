package com.idtech.nuosucivilaid.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.idtech.nuosucivilaid.dto.ChangePasswordRequest;
import com.idtech.nuosucivilaid.dto.LoginRequest;
import com.idtech.nuosucivilaid.dto.RegisterRequest;
import com.idtech.nuosucivilaid.dto.UpdateUserInfoRequest;
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
        log.info("--- 登录尝试 --- 用户名: {}", request.getUsername());

        // 1. 验证验证码 (忽略大小写)
        if (!captchaManager.verifyCaptcha(request.getCaptchaId(), request.getCaptcha())) {
            log.warn("验证码校验失败: 预期ID={}, 输入值={}", request.getCaptchaId(), request.getCaptcha());
            throw BusinessException.loginCaptchaError();
        }

        // 2. 查询用户
        SysUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("登录失败: 用户 {} 不存在", request.getUsername());
                    return BusinessException.loginAuthFailed();
                });

        // 3. 校验密码 (关键调试点)
        log.debug("数据库存储的密文: {}", user.getPassword());
        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isMatch) {
            log.warn("登录失败: 用户 {} 密码不匹配", request.getUsername());
            throw BusinessException.loginAuthFailed();
        }

        // 4. 校验账号状态
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            log.warn("登录失败: 用户 {} 已被逻辑删除", request.getUsername());
            throw BusinessException.loginForbidden();
        }

        // 5. 查询角色
        SysRole role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> {
                    log.error("数据异常: 用户 {} 关联的角色ID {} 不存在", user.getUsername(), user.getRoleId());
                    return BusinessException.loginRoleNotFound();
                });

        // 6. 生成 Token
        boolean rememberMe = Boolean.TRUE.equals(request.getRememberMe());
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), role.getRoleCode(), rememberMe);

        // 7. 组装结果
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .roleName(role.getRoleName())
                .roleCode(role.getRoleCode())
                .build();

        log.info("登录成功: 用户={}, 角色={}", user.getUsername(), role.getRoleCode());

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(rememberMe ? jwtUtil.getRememberMeExpiration() : jwtUtil.getExpiration())
                .userInfo(userInfo)
                .build();
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        log.info("开始注册新用户: {}", request.getUsername());

        if (!captchaManager.verifyCaptcha(request.getCaptchaId(), request.getCaptcha())) {
            throw BusinessException.loginCaptchaError();
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw BusinessException.registerPasswordNotMatch();
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw BusinessException.registerUserExist();
        }

        // 获取默认角色 'user'
        SysRole defaultRole = roleRepository.findByRoleCode("user")
                .orElseThrow(BusinessException::registerUserRoleNotFound);

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
        log.info("注册成功: 用户ID={}", user.getId());
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw BusinessException.authFailed();
        }

        String username = authentication.getName();
        SysUser user = userRepository.findByUsername(username).orElseThrow(BusinessException::notFound);
        SysRole role = roleRepository.findById(user.getRoleId()).orElseThrow(BusinessException::notFound);

        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .roleName(role.getRoleName())
                .roleCode(role.getRoleCode())
                .build();
    }

    @Override
    @Transactional
    public UserInfoVO updateCurrentUserInfo(UpdateUserInfoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw BusinessException.authFailed();
        }

        String username = authentication.getName();
        SysUser user = userRepository.findByUsername(username).orElseThrow(BusinessException::notFound);

        // 如果修改了用户名，校验用户名是否已被占用
        if (StrUtil.isNotBlank(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw BusinessException.registerUserExist();
            }
            user.setUsername(request.getUsername());
        }

        // 如果修改了手机号，校验手机号是否已被其他用户占用
        if (StrUtil.isNotBlank(request.getMobile()) && !request.getMobile().equals(user.getMobile())) {
            if (!Validator.isMobile(request.getMobile())) {
                throw BusinessException.registerPhoneFormatError();
            }
            userRepository.findByMobile(request.getMobile()).ifPresent(u -> {
                if (!u.getId().equals(user.getId())) {
                    throw BusinessException.registerPhoneExist();
                }
            });
            user.setMobile(request.getMobile());
        }

        user.setUpdateTime(Instant.now());
        userRepository.save(user);

        SysRole role = roleRepository.findById(user.getRoleId()).orElseThrow(BusinessException::notFound);

        log.info("用户信息更新成功: 用户ID={}", user.getId());

        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .roleName(role.getRoleName())
                .roleCode(role.getRoleCode())
                .build();
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw BusinessException.authFailed();
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw BusinessException.changePasswordNotMatch();
        }

        String username = authentication.getName();
        SysUser user = userRepository.findByUsername(username).orElseThrow(BusinessException::notFound);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw BusinessException.changePasswordOriginalWrong();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdateTime(Instant.now());
        userRepository.save(user);

        log.info("用户密码修改成功: 用户ID={}", user.getId());
    }
}