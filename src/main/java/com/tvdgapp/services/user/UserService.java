package com.tvdgapp.services.user;

import com.tvdgapp.dtos.auth.ResetPasswordDto;

import java.util.List;

public interface UserService {

    void publishForgotPasswordEmail(String email, String url);
    void resetPassword(ResetPasswordDto resetPasswordDto);

    List<String> getUserTokensByRole(String role);

//    List<String> getUserEmailsByRole(String role);
}
