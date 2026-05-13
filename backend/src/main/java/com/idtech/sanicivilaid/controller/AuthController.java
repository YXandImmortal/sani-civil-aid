package com.idtech.sanicivilaid.controller;

import com.idtech.sanicivilaid.dto.ChangePasswordRequest;
import com.idtech.sanicivilaid.dto.LoginRequest;
import com.idtech.sanicivilaid.dto.RegisterRequest;
import com.idtech.sanicivilaid.dto.UpdateUserInfoRequest;
import com.idtech.sanicivilaid.enums.ResultCode;
import com.idtech.sanicivilaid.service.AuthService;
import com.idtech.sanicivilaid.util.JwtUtil;
import com.idtech.sanicivilaid.vo.LoginResponse;
import com.idtech.sanicivilaid.vo.Result;
import com.idtech.sanicivilaid.vo.UserInfoVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("用户登录请求，用户名: {}", request.getUsername());
        LoginResponse response = authService.login(request);
        log.info("用户登录成功，用户名: {}", request.getUsername());
        return Result.success(ResultCode.LOGIN_SUCCESS, response);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid RegisterRequest request) {
        log.info("用户注册请求，用户名: {}", request.getUsername());
        authService.register(request);
        log.info("用户注册成功，用户名: {}", request.getUsername());
        return Result.success(ResultCode.REGISTER_SUCCESS, null);
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getCurrentUserInfo() {
        log.debug("获取当前用户信息");
        UserInfoVO userInfo = authService.getCurrentUserInfo();
        log.debug("获取当前用户信息成功，用户名: {}", userInfo.getUsername());
        return Result.success(userInfo);
    }

    @PutMapping("/info")
    public Result<LoginResponse> updateCurrentUserInfo(@RequestBody @Valid UpdateUserInfoRequest request,
                                                       @RequestHeader("Authorization") String authHeader) {
        log.info("更新当前用户信息请求");
        String oldUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfoVO userInfo = authService.updateCurrentUserInfo(request);

        String token = authHeader.substring(7);
        long expiresIn = jwtUtil.getExpiration();

        // 如果修改了用户名，刷新 token
        if (request.getUsername() != null && !request.getUsername().equals(oldUsername)) {
            token = jwtUtil.refreshToken(token, userInfo.getUsername());
            boolean rememberMe = Boolean.TRUE.equals(jwtUtil.extractRememberMe(token));
            expiresIn = rememberMe ? jwtUtil.getRememberMeExpiration() : jwtUtil.getExpiration();
        }

        log.info("更新当前用户信息成功，用户名: {}", userInfo.getUsername());
        return Result.success(LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .userInfo(userInfo)
                .build());
    }

    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        log.info("修改密码请求");
        authService.changePassword(request);
        log.info("修改密码成功");
        return Result.success();
    }
}
