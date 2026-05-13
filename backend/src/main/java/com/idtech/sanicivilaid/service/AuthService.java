package com.idtech.sanicivilaid.service;

import com.idtech.sanicivilaid.dto.ChangePasswordRequest;
import com.idtech.sanicivilaid.dto.LoginRequest;
import com.idtech.sanicivilaid.dto.RegisterRequest;
import com.idtech.sanicivilaid.dto.UpdateUserInfoRequest;
import com.idtech.sanicivilaid.vo.LoginResponse;
import com.idtech.sanicivilaid.vo.UserInfoVO;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    UserInfoVO getCurrentUserInfo();

    UserInfoVO updateCurrentUserInfo(UpdateUserInfoRequest request);

    void changePassword(ChangePasswordRequest request);
}
