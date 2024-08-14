package com.tvdgapp.controllers.user.rider;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.rider.*;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.models.user.rider.RiderUser;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.rider.RiderUserService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.UserInfoUtil;
import com.tvdgapp.validators.ValidationErrors;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.tvdgapp.validators.ValidatePassword.validateSamePassword;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-rider}")
public class RiderUserController implements IRiderUserController {
    public static final int MAX_PROFILE_PIC_UPLOAD_SIZE = 1024 * 1024 * 2; //2mb
    private final RiderUserService service;
    private final UserInfoUtil userInfoUtil;

    @Override
    @PostMapping("/signup")
    @PreAuthorize("hasAnyAuthority('riderUserManagement', 'ADMIN')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> createRiderUser(@Valid @RequestBody RiderUserDto riderUserDto) {
        RiderUser riderUser = this.service.createRiderUser(riderUserDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(riderUser.getId()), "Resource created successfully");
    }


    @GetMapping
//    @PreAuthorize("hasAnyAuthority('manageAdminUser', 'viewAdminUser','manageMailingList')")
    @PreAuthorize("hasAuthority('riderUserManagement')")
    public ResponseEntity<ApiDataResponse<Page<ListRiderUserDto>>> listUsers(Pageable pageable){
        Page<ListRiderUserDto> list = this.service.listUsers(pageable);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @PostMapping("/changepassword")
    public ResponseEntity<ApiDataResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
        validateSamePassword(changePasswordDto);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        this.service.changePassword(userInfo.getUsername(), changePasswordDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
    }
    @Override
    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<RiderUserDetailResponseDto>> fetchRiderUserDetail()  {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        RiderUserDetailResponseDto userDetailResponseDto = this.service.fetchRiderUserDetail(userInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK,userDetailResponseDto, "Resource retrieved successfully");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasAuthority('riderUserManagement')")
    public ResponseEntity<ApiDataResponse<List<RiderUserResponseDto>>> getAllRiderUsers() {
        List<RiderUserResponseDto> list = this.service.getAllRiderUsers();
        return ApiResponseUtils.response(HttpStatus.OK, list,"Resource retrieved successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('riderUserManagement')")
    public ResponseEntity<ApiDataResponse<Optional<RiderUserResponseDto>>> getRiderUserById(@PathVariable Long id) {
        return ApiResponseUtils.response(HttpStatus.OK, this.service.getRiderUserById(id), "Resource successfully retrieved");

    }

    @Override
    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> updateRiderUserProfile(@Valid @RequestBody UpdateRiderUserDetailDto riderUserDto) {
        var devUserInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        this.service.updateRiderUser(devUserInfo.getUser().getId(), riderUserDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource updated successfully");
    }
    @PutMapping("/profile/{userId}")
    @PreAuthorize("hasAuthority('riderUserManagement')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> updateRiderUserProfileById(@PathVariable Long userId, @Valid @RequestBody UpdateRiderUserDetailDto riderUserDto) {
        this.service.updateRiderUser(userId, riderUserDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource updated successfully");
    }

    @Override
    @PutMapping("/profilePic")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<FileUrlDto>> updateProfilePic(@RequestPart(value = "profile_pic_upload") MultipartFile profileFileUpload) {
        validateProfilePicUpload(profileFileUpload);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        String fileUrl = this.service.updateProfilePic(userInfo.getUser().getId(), profileFileUpload).orElseThrow(() ->
                new InvalidRequestException("unable to update file"));
        return ApiResponseUtils.response(HttpStatus.OK, new FileUrlDto(fileUrl), "Resource updated successfully");
    }
    @Override
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('riderUserManagement')")
    public ResponseEntity<ApiDataResponse<String>> deleteUser(@PathVariable Long userId) throws Exception {
        this.service.deleteRiderUserById(userId);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }

    private void validateProfilePicUpload(MultipartFile profileFileUpload) {
        ValidationErrors validationErrors = new ValidationErrors();
        List<String> mimeTypes = List.of("jpg", "jpeg", "gif", "png");
        FileUploadValidatorUtils.validateRequired(profileFileUpload, validationErrors);
        FileUploadValidatorUtils.validateFileType(profileFileUpload, mimeTypes, validationErrors);
        FileUploadValidatorUtils.validateSize(profileFileUpload, MAX_PROFILE_PIC_UPLOAD_SIZE, validationErrors);
        if (validationErrors.hasErrors()) {
            throw new InvalidRequestException(validationErrors);
        }

    }


}