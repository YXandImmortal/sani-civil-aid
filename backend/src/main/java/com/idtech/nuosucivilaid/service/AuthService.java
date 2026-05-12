package com.idtech.nuosucivilaid.service;

import com.idtech.nuosucivilaid.dto.ChangePasswordRequest;
import com.idtech.nuosucivilaid.dto.LoginRequest;
import com.idtech.nuosucivilaid.dto.RegisterRequest;
import com.idtech.nuosucivilaid.dto.UpdateUserInfoRequest;
import com.idtech.nuosucivilaid.vo.LoginResponse;
import com.idtech.nuosucivilaid.vo.UserInfoVO;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    UserInfoVO getCurrentUserInfo();

    UserInfoVO updateCurrentUserInfo(UpdateUserInfoRequest request);

    void changePassword(ChangePasswordRequest request);
}
