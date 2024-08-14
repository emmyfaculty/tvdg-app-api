package com.tvdgapp.controllers.user.admin;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.dtos.user.UpdateUserProfileDto;
import com.tvdgapp.dtos.user.UserProfileDto;
import com.tvdgapp.dtos.user.admin.AdminUserDetailDto;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = { "Admin User Account Controller" })
public interface IAdminUserAccountController {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Fetch user profile", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<UserProfileDto>> fetchUserProfile();

//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Password changed successfully"),
//            @ApiResponse(code = 400, message = "Invalid request!!!"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Change Password", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Update profile(personal info) for current authenticated user", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<AdminUserDetailDto>> updateProfile(
            @Valid @RequestBody UpdateUserProfileDto dto);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Update profile pic for current authenticated user", authorizations = {@Authorization(value = "JWT") })
    @ApiImplicitParam(name = "profile_pic_upload", value = "Accepted file types:png,jpg,jpeg max size:2mb", dataTypeClass = MultipartFile.class, paramType = "body")
    ResponseEntity<ApiDataResponse<FileUrlDto>> updateProfilePic(@RequestPart(value = "profile_pic_upload") MultipartFile profileFileUpload);
}
