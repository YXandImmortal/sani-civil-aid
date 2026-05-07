package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.dto.LoginRequest;
import com.idtech.nuosucivilaid.dto.RegisterRequest;
import com.idtech.nuosucivilaid.enums.ResultCode;
import com.idtech.nuosucivilaid.service.AuthService;
import com.idtech.nuosucivilaid.vo.LoginResponse;
import com.idtech.nuosucivilaid.vo.Result;
import com.idtech.nuosucivilaid.vo.UserInfoVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

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
}
