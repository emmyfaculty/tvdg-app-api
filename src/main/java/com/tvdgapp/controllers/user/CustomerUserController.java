package com.tvdgapp.controllers.user;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.user.*;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.user.CustomerUserService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.UserInfoUtil;
import com.tvdgapp.validators.ValidationErrors;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("${api.basepath-customer}")
@Api(tags = { "Customer User Controller" })
public class CustomerUserController {
    public static final int MAX_PROFILE_PIC_UPLOAD_SIZE = 1024 * 1024 * 2; //2mb
    private final CustomerUserService service;
    private final UserInfoUtil userInfoUtil;

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Resource created successfully"),
            @ApiResponse(code = 400, message = "Invalid request!!!"),
            @ApiResponse(code = 409, message = "non unique entity!!!"),
            @ApiResponse(code = 401, message = "not authorized!")})
    @ApiOperation(value = "Create user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('manageCustomer', 'ADMIN')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> createCustomer(@Valid @RequestBody CustomerUserDto customerUserDto) {
        CustomerUser customerUser = this.service.createCustomerUser(customerUserDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(customerUser.getId()), "Resource created successfully");
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('customerUserManagement', 'CUSTOMER')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> updateCustomerUser(
            @Valid @RequestBody UpdateCustomerUserDto updateCustomerUserDto) {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        CustomerUser updatedCustomerUser = service.updateCustomerUser(userInfo.getUserId(),updateCustomerUserDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(updatedCustomerUser.getId()), "The User Details has been updated successfully");
    }
    @PutMapping("/account_info")
    @PreAuthorize("hasAnyAuthority('customerUserManagement', 'CUSTOMER')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> updateCustomerUser(@Valid @RequestBody UpdateCustomerAccountInfoDto updateDto) {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        CustomerUser updatedCustomerUser = service.updateCustomerUser(userInfo.getUserId(), updateDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(updatedCustomerUser.getId()), "The User Account Details has been updated successfully");
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('manageCustomer', 'ADMIN')")
    public ResponseEntity<ApiDataResponse<List<CustomerUserDtoResponse>>> getAllCustomerUsers() {
        List<CustomerUserDtoResponse> list = this.service.getAllCustomerUsers();
        return ApiResponseUtils.response(HttpStatus.OK, list,"Resource retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('manageCustomer', 'ADMIN')")
    public ResponseEntity<ApiDataResponse<CustomerUserDtoResponse>> getCustomerUserById(@PathVariable Long id) {
        return ApiResponseUtils.response(HttpStatus.OK, this.service.getCustomerUserById(id), "Resource successfully retrieved");
    }

    @PostMapping("/changepassword")
    public ResponseEntity<ApiDataResponse<Object>> changePassword(ChangePasswordDto changePasswordDto) throws Exception {
        validateSamePassword(changePasswordDto);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        this.service.changePassword(userInfo.getUsername(), changePasswordDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('customerUserManagement', 'CUSTOMER')")
    public ResponseEntity<ApiDataResponse<CustomerUserDetailDto>> retrieveCustomerUserDetail() {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        CustomerUserDetailDto detailDto = this.service.fetchCustomerUserDetail(userInfo.getUser().getId());
        return ApiResponseUtils.response(HttpStatus.OK, detailDto, "Resource retrieved successfully");
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('customerUserManagement', 'CUSTOMER')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> updateCustomerUserProfile(@Valid @RequestPart("customerUser")CustomerUserDto engineerRequestDto, @Valid @RequestPart("profilePic")MultipartFile profileFileUpload) {
        this.validate(profileFileUpload);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        CustomerUser customerUser = this.service.updateCustomerUser(userInfo.getUser().getId(), engineerRequestDto, profileFileUpload);
        return ApiResponseUtils.response(HttpStatus.OK,new IdResponseDto(customerUser.getId()), "Resource updated successfully");
    }

    @PutMapping("/profilePic")
    @PreAuthorize("hasAnyAuthority('customerUserManagement', 'CUSTOMER')")
    public ResponseEntity<ApiDataResponse<FileUrlDto>> updateProfilePic(@RequestPart(value = "profile_pic_upload") MultipartFile profileFileUpload) {
        validateProfilePicUpload(profileFileUpload);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        String fileUrl = this.service.updateProfilePic(userInfo.getUser().getId(), profileFileUpload).orElseThrow(() ->
                new InvalidRequestException("unable to update file"));
        return ApiResponseUtils.response(HttpStatus.OK, new FileUrlDto(fileUrl), "Resource updated successfully");
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

        private void validate (MultipartFile profileFileUpload){

            ValidationErrors validationErrors = new ValidationErrors();

            if (profileFileUpload != null) {
                List<String> mimeTypes = List.of("jpg", "jpeg", "gif", "png");
                FileUploadValidatorUtils.validateRequired(profileFileUpload, validationErrors);
                FileUploadValidatorUtils.validateFileType(profileFileUpload, mimeTypes, validationErrors);
                FileUploadValidatorUtils.validateSize(profileFileUpload, MAX_PROFILE_PIC_UPLOAD_SIZE, validationErrors);
            }

            if (validationErrors.hasErrors()) {
                throw new InvalidRequestException(validationErrors);
            }
        }


}