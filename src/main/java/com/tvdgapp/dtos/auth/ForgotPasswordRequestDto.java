package com.tvdgapp.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequestDto {
    @NotBlank
    @Email
    private String email;
    private String url;

}
