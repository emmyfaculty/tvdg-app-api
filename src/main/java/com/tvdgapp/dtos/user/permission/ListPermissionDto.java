package com.tvdgapp.dtos.user.permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListPermissionDto {
    private String group;
    private Collection<PermissionDto> permissions;
}
