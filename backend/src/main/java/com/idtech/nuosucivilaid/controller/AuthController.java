package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.dto.LoginRequest;
import com.idtech.nuosucivilaid.dto.RegisterRequest;
import com.idtech.nuosucivilaid.enums.ResultCode;
import com.idtech.nuosucivilaid.service.AuthService;
import com.idtech.nuosucivilaid.vo.LoginResponse;
import com.idtech.nuosucivilaid.vo.Result;
import com.idtech.nuosucivilaid.vo.UserInfoVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(ResultCode.LOGIN_SUCCESS, response);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return Result.success(ResultCode.REGISTER_SUCCESS, null);
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getCurrentUserInfo() {
        UserInfoVO userInfo = authService.getCurrentUserInfo();
        return Result.success(userInfo);
    }
}
