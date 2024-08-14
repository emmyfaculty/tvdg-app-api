package com.tvdgapp.controllers.user;


import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.affiliate.*;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.user.admin.AdminUserDetailResponseDto;
import com.tvdgapp.dtos.user.admin.AdminUserRequestDto;
import com.tvdgapp.dtos.user.admin.ListAdminUserDto;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Api(tags = { "Affiliate User Controller" })
public interface IAffiliateUserController {

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Resource created successfully"),
            @ApiResponse(code = 400, message = "Invalid request!!!"),
            @ApiResponse(code = 409, message = "non unique entity!!!"),
            @ApiResponse(code = 401, message = "not authorized!")})
    @ApiOperation(value = "Create user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<IdResponseDto>> createAffiliateUser(@Valid @RequestBody AffiliateUserDto affiliateUserDto);


//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resources retrieved successfully"),
//            @ApiResponse(code = 401, message = "not authorized!")})
//    @ApiOperation(value = "List users", notes = "authorities[manageAdminUser, viewAdminUser,manageMailingList]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Page<ListAffiliateUserDto>>> listUsers(Pageable pageable);
//
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Fetch admin user detail", notes = "authorities[manageAdminUser, viewAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<AffiliateUserDetailResponseDto>> fetchAffiliateUserDetail();
//
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resource updated successfully"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 403, message = "forbidden!!!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Update admin user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Object>> updateAffiliateUser(
//            @Valid @RequestBody AffiliateUserDto dto, @PathVariable Long userId);
//
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resource deactivated successfully"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 403, message = "forbidden!!!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Deactivate user account", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Object>> deactivateUserAccount(@PathVariable Long userId);
//
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resource deactivated successfully"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 403, message = "forbidden!!!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Activate user account", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Object>> activateUserAccount(@PathVariable Long userId);


    //    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 403, message = "forbidden!!!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Get admin user tracking data ", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<UserTrackingDto>> getAdminUserTrackingData(@RequestParam(required = false) String period);
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resource deactivated successfully"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 403, message = "forbidden!!!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Activate user account", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<AffiliateDashBoardDto>> retrieveAffiliateDashboard(@RequestParam Long affiliateId);
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resource deactivated successfully"),
//            @ApiResponse(code = 401, message = "not authorized!"),
//            @ApiResponse(code = 403, message = "forbidden!!!"),
//            @ApiResponse(code = 404, message = "not found!!!")})
//    @ApiOperation(value = "Activate user account", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Object>> initiateWithdrawal(@RequestParam Long affiliateId, @RequestBody WithdrawalRequestDto withdrawalRequestDto) throws Exception;
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource deactivated successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Activate user account", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<IdResponseDto>> updateAffiliateUserProfile(@Valid @RequestBody UpdateAffiliateUserDetailDto affiliateUserDto);
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource deactivated successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Activate user account", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<FileUrlDto>> updateProfilePic(@RequestPart(value = "profile_pic_upload") MultipartFile profileFileUpload);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Get admin user tracking data ", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<String>> deleteUser( @PathVariable Long userId) throws Exception;

}