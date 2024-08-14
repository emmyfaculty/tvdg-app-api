package com.tvdgapp.services;

import com.tvdgapp.dtos.auth.LoginResponseDto;
import com.tvdgapp.dtos.auth.LogoutRequest;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponseDto login(String username, String password, HttpServletRequest request);
    void changePassword(String user, ChangePasswordDto changePasswordDto);
    void logout(String token, LogoutRequest logoutRequest);
    LoginResponseDto verifyOtp(String username, String otp, HttpServletRequest request);



}
