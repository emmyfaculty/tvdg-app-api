package com.tvdgapp.dtos.user.permission;


import com.tvdgapp.models.user.PermissionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionRequestDto {
    private Integer id;

    @NotEmpty(message = "Permission key must not be empty")
    private String permission;

    @NotEmpty(message = "Name must not be empty")
    private String name;

    private String description;

    @NotNull(message = "Permission type must not be null")
    private PermissionType permissionType;

    private String permissionGroup;

}
