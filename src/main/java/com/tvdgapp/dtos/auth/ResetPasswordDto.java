package com.tvdgapp.dtos.auth;

import lombok.Data;

@Data
public class ResetPasswordDto {

    private String newPassword;
    private String token;
}
