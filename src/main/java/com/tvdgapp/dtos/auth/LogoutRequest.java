package com.tvdgapp.dtos.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogoutRequest {
    private String refreshToken;
}