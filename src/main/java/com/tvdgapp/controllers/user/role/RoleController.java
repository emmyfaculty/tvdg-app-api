package com.tvdgapp.controllers.user.role;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.user.role.ListRoleDto;
import com.tvdgapp.dtos.user.role.RoleDetailDto;
import com.tvdgapp.dtos.user.role.RoleRequestDto;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.services.user.RoleService;
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
@RequestMapping("${api.basepath-admin}/roles")
public class RoleController implements IRoleController {

    private final RoleService roleService;

    @Override
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> createRole(@Valid @RequestBody RoleRequestDto roleDto) {
        Role role = roleService.createRole(roleDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(role.getId().longValue()), "Resource created successfully");
    }


    @Override
    @GetMapping("/manage")
    @PreAuthorize("hasAnyAuthority('manageRole', 'viewRole')")
    public ResponseEntity<ApiDataResponse<Collection<ListRoleDto>>> listAllRoles() throws Exception {
        Collection<ListRoleDto> list = this.roleService.manageRoles();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }


    @Override
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('manageRole', 'viewRole')")
    public ResponseEntity<ApiDataResponse<Collection<ListRoleDto>>> listRoles(@RequestParam(name = "type", required = false) String type) throws Exception {
        Collection<ListRoleDto> list = this.roleService.listRoles(type);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @Override
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('manageRole', 'viewRole')")
    public ResponseEntity<ApiDataResponse<RoleDetailDto>> fetchRoleDetail(@PathVariable int roleId) throws Exception {
        RoleDetailDto roleDetailDto = this.roleService.fetchRoleDetail(roleId);
        return ApiResponseUtils.response(HttpStatus.OK,roleDetailDto, "Resource retrieved successfully");
    }

    @Override
    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<Object>> updateRole(
            @Valid @RequestBody RoleRequestDto dto, @PathVariable int roleId) throws Exception {
        Role role = this.roleService.updateRole(roleId, dto);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource updated successfully");
    }

    @Override
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('manageRole')")
    public ResponseEntity<ApiDataResponse<String>> deleteRole(@PathVariable int roleId) throws Exception {
        this.roleService.deleteRole(roleId);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }

}
