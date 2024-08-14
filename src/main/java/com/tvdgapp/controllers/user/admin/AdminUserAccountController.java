package com.tvdgapp.controllers.user.admin;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.dtos.user.UpdateUserProfileDto;
import com.tvdgapp.dtos.user.UserProfileDto;
import com.tvdgapp.dtos.user.admin.AdminUserDetailDto;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.user.admin.AdminUserService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.UserInfoUtil;
import com.tvdgapp.validators.ValidationErrors;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.tvdgapp.validators.ValidatePassword.validateSamePassword;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-api}/admin/user/account")
public class AdminUserAccountController implements IAdminUserAccountController {

    public static final int MAX_PROFILE_PIC_SIZE = 1024 * 1024 * 2; //2mb
    private final UserInfoUtil userInfoUtil;
    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('manageAdmin')")
    public ResponseEntity<ApiDataResponse<UserProfileDto>> fetchUserProfile() {

        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        UserProfileDto userProfileDto = this.adminUserService.fetchAdminUserProfile(userInfo.getUsername());
        return ApiResponseUtils.response(HttpStatus.OK, userProfileDto, "Resource retrieved successfully");
    }

//    @Override
//    @PostMapping("/changepassword")
//    public ResponseEntity<ApiDataResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
//
//        validateSamePassword(changePasswordDto);
//
//        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
//        this.adminUserService.changePassword(userInfo.getUsername(), changePasswordDto);
//        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
//    }

    @Override
    @PutMapping
    @PreAuthorize("hasAuthority('manageAdmin')")
    public ResponseEntity<ApiDataResponse<AdminUserDetailDto>> updateProfile(
            @Valid @RequestBody UpdateUserProfileDto dto){
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        AdminUserDetailDto responseDto = this.adminUserService.updateProfile(userInfo.getUserId(),dto);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto,"Resource updated successfully");
    }
    @Override
    @PutMapping("/profilePic")
    @PreAuthorize("hasAuthority('manageAdmin')")
    public ResponseEntity<ApiDataResponse<FileUrlDto>> updateProfilePic(@RequestPart(value = "profile_pic_upload") MultipartFile profileFileUpload) {
        validateProfilePicUpload(profileFileUpload);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        String fileUrl= this.adminUserService.updateProfilePic(userInfo.getUser().getId(),profileFileUpload).orElseThrow(()->
                new InvalidRequestException("unable to update file"));
        return ApiResponseUtils.response(HttpStatus.OK,new FileUrlDto(fileUrl), "Resource updated successfully");
    }

    private void validateProfilePicUpload(MultipartFile profileFileUpload) {
        ValidationErrors validationErrors = new ValidationErrors();
        List<String> mimeTypes = List.of("jpg", "jpeg", "gif", "png");
        FileUploadValidatorUtils.validateRequired(profileFileUpload, validationErrors);
        FileUploadValidatorUtils.validateFileType(profileFileUpload, mimeTypes, validationErrors);
        FileUploadValidatorUtils.validateSize(profileFileUpload, MAX_PROFILE_PIC_SIZE, validationErrors);
        if (validationErrors.hasErrors()) {
            throw new InvalidRequestException(validationErrors);
        }
    }

}
