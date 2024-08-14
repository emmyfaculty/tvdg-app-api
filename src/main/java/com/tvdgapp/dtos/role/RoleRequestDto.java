package com.tvdgapp.dtos.role;

import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.validators.Enum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDto {
    private Collection<String> permissions;
    @Enum(enumClass= RoleType.class)
    @NotEmpty(message = "Field is required")
    private String roleType;
    @NotEmpty
    @Size(max=100)
    private String name;
    @Size(max=255)
    private String description;

}
