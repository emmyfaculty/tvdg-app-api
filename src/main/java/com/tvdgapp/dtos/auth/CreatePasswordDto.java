package com.tvdgapp.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreatePasswordDto {
    private String token;
    @NotBlank(message = "message should not blank")
    @Size(max=64)
    private String password;
    private String confirmPassword;
}
