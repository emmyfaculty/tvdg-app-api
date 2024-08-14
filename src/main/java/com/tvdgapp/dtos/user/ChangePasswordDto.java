package com.tvdgapp.dtos.user;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ChangePasswordDto {

    private String password;
    @Size(min =6)
    private String newPassword;
    private String confirmPassword;
}
