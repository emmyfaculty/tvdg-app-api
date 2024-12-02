package com.tvdgapp.mapper;

import com.tvdgapp.dtos.user.permission.PermissionRequestDto;
import com.tvdgapp.models.user.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionRequestDto convertToDTO(Permission permission) {
        PermissionRequestDto dto = new PermissionRequestDto();
        dto.setId(permission.getId());
        dto.setPermission(permission.getPermission());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
//        dto.setPermissionType(permission.getPermissionType());
//        dto.setPermissionGroup(permission.getPermissionGroup());
        // You can map other fields as needed

        return dto;
    }
}
