package com.tvdgapp.controllers.user.permission;


import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.user.permission.ListPermissionDto;
import com.tvdgapp.dtos.user.permission.PermissionRequestDto;
import com.tvdgapp.models.user.Permission;
import com.tvdgapp.services.user.PermissionService;
import com.tvdgapp.utils.ApiResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-api}/admin")
public class PermissionController implements IPermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<Permission>> createPermission(@Valid @RequestBody PermissionRequestDto permissionDTO) {
        Permission createdPermission = permissionService.createPermission(permissionDTO);
        return ApiResponseUtils.response(HttpStatus.OK, createdPermission, "Permission created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<PermissionRequestDto>> updatePermission(@PathVariable Integer id, @Valid @RequestBody PermissionRequestDto permissionDTO) {
        PermissionRequestDto updatedPermission = permissionService.updatePermission(id, permissionDTO);
        return ApiResponseUtils.response(HttpStatus.OK, updatedPermission, "Permission Updated successfully");

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<Void>> deletePermission(@PathVariable Integer id) {
        permissionService.deletePermission(id);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }

    @PostMapping("/{permissionId}/assign/{roleId}")
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<Void>> assignPermissionToRole(@PathVariable Integer permissionId, @PathVariable Integer roleId) {
        permissionService.assignPermissionToRole(permissionId, roleId);
        return ApiResponseUtils.response(HttpStatus.OK, "Permission assigned to role successfully");
    }

    @Override
    @GetMapping("/permissions")
    @PreAuthorize("hasAnyAuthority('manageRole', 'viewRole')")
    public ResponseEntity<ApiDataResponse<Collection<ListPermissionDto>>> listPermissions() throws Exception {
        Collection<ListPermissionDto> list = this.permissionService.listPermissions();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @GetMapping("/user-permissions")
    @PreAuthorize("hasAnyAuthority('manageRole', 'viewRole')")
    public ResponseEntity<ApiDataResponse<Collection<String>>> listUsersPermissions(@RequestParam(name = "email") String email) throws Exception {
        Collection<String> list = this.permissionService.listPermissions(email);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

}
