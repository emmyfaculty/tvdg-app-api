package com.tvdgapp.dtos.user.admin;


import com.tvdgapp.dtos.user.UserDto;
import com.tvdgapp.models.user.admin.GenerateCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Collection;

@Data
public class AdminUserRequestDto extends UserDto {
    @NotEmpty
    Collection<Integer> roles;
    @NotNull(message = "Generate code option is required")
    private String generateCode; // "yes" or "no"
}
