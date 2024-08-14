package com.tvdgapp.controllers.user.admin;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.user.admin.AdminUserDetailResponseDto;
import com.tvdgapp.dtos.user.admin.AdminUserRequestDto;
import com.tvdgapp.dtos.user.admin.ListAdminUserDto;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.services.user.admin.AdminUserService;
import com.tvdgapp.utils.ApiResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-api}/admin/users")
public class AdminUserController implements IAdminUserController {

    private final AdminUserService adminUserService;

    @Override
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('manageAdminUser')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> createUser(@Valid @RequestBody AdminUserRequestDto adminUserRequestDto) {
        AdminUser adminUser = this.adminUserService.createUser(adminUserRequestDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(adminUser.getId()), "Resource created successfully");
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyAuthority('manageAdminUser', 'viewAdminUser','manageMailingList')")
    public ResponseEntity<ApiDataResponse<Page<ListAdminUserDto>>> listUsers(Pageable pageable){
        Page<ListAdminUserDto> list = this.adminUserService.listUsers(pageable);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @Override
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('manageAdminUser', 'viewAdminUser')")
    public ResponseEntity<ApiDataResponse<AdminUserDetailResponseDto>> fetchAdminUserDetail(@PathVariable Long userId)  {
        AdminUserDetailResponseDto userDetailResponseDto = this.adminUserService.fetchAdminUserDetail(userId);
        return ApiResponseUtils.response(HttpStatus.OK,userDetailResponseDto, "Resource retrieved successfully");
    }

    @Override
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('manageAdminUser')")
    public ResponseEntity<ApiDataResponse<Object>> updateAdminUser(
            @Valid @RequestBody AdminUserRequestDto dto,@PathVariable Long userId){
        this.adminUserService.updateAdminUser(userId,dto);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource updated successfully");
    }

    @Override
    @PutMapping("{userId}/deactivate")
    @PreAuthorize("hasAuthority('manageAdminUser')")
    public ResponseEntity<ApiDataResponse<Object>> deactivateUserAccount(@PathVariable Long userId)  {
        this.adminUserService.deactivateUser(userId);
        return ApiResponseUtils.response(HttpStatus.OK,"Resource deactivated successfully");
    }

    @Override
    @PutMapping("{userId}/activate")
    @PreAuthorize("hasAuthority('manageAdminUser')")
    public ResponseEntity<ApiDataResponse<Object>> activateUserAccount(@PathVariable Long userId)  {
        this.adminUserService.activateUser(userId);
        return ApiResponseUtils.response(HttpStatus.OK,"Resource activated successfully");
    }

    @Override
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('manageAdminUser')")
    public ResponseEntity<ApiDataResponse<String>> deleteUser(@PathVariable Long userId) throws Exception {
        this.adminUserService.deleteUser(userId);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }

}
