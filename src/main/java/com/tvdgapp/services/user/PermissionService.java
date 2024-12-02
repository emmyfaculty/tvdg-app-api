package com.tvdgapp.services.user;


import com.tvdgapp.dtos.user.permission.ListPermissionDto;
import com.tvdgapp.dtos.user.permission.PermissionRequestDto;
import com.tvdgapp.models.user.Permission;
import com.tvdgapp.models.user.PermissionType;
import com.tvdgapp.services.generic.TvdgEntityService;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.Optional;

public interface PermissionService  extends TvdgEntityService<Integer, Permission> {
    Permission createPermission(@Valid PermissionRequestDto permissionDTO);
    PermissionRequestDto updatePermission(Integer permissionId, @Valid PermissionRequestDto permissionDTO);
    void deletePermission(Integer permissionId);
    void assignPermissionToRole(Integer permissionId, Integer roleId);
    Optional<Permission> findPermByKey(String anyString);
    Collection<Permission> fetchPermissions();
    Optional<Permission> fetchByPermission(String permission);

    Collection<Permission> findByPermissionIn(Collection<String> names);

//    Collection<Permission> fetchPermissions(PermissionType permissionType);

    Collection<ListPermissionDto> listPermissions();
    Collection<String> listPermissions(String email);
}
