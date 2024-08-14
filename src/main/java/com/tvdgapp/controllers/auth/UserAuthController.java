package com.tvdgapp.controllers.auth;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.auth.*;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.securityconfig.JwtTokenProvider;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.AuthService;
import com.tvdgapp.services.auth.RefreshTokenService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.UserInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tvdgapp.utils.CommonUtils.castToNonNull;
import static com.tvdgapp.validators.ValidatePassword.validateSamePassword;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "${api.basepath-api}/user")
@Api(tags = { "User Authentication Controller" })
public class UserAuthController {

    private final AuthService userAuthService;
    private final UserInfoUtil userInfoUtil;
    private final JwtTokenProvider authTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @ApiResponses(value = {@ApiResponse(code = 400, message = "not authorized!")})
    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<LoginResponseDto>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws Exception {
        LoginResponseDto loginResponseDto = this.userAuthService.login(loginRequest.getUsername(),loginRequest.getPassword(), request);
        return ApiResponseUtils.response(HttpStatus.OK, loginResponseDto, "Login successfully");

    }

    @PostMapping("/changepassword")
    public ResponseEntity<ApiDataResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception {

        validateSamePassword(changePasswordDto);

        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        this.userAuthService.changePassword(userInfo.getUsername(), changePasswordDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
    }

    @ApiResponses(value = {@ApiResponse(code = 401, message = "not authorized!")})
    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse<Object>> logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request) throws Exception {
         String token=authTokenProvider.extractTokenFromRequest(request);
         if(StringUtils.isNotEmpty(token)) {
             this.userAuthService.logout(castToNonNull(token), logoutRequest);
         }
        return ApiResponseUtils.response(HttpStatus.OK, "Logout successfully");
    }

    @ApiResponses(value = {@ApiResponse(code = 401, message = "not authorized!")})
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiDataResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String token = this.refreshTokenService.refreshUserToken(refreshTokenRequest);
        return ApiResponseUtils.response(HttpStatus.OK, new RefreshTokenResponse(token));
    }


    @PostMapping("/verify_otp")
    public ResponseEntity<ApiDataResponse<LoginResponseDto>> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest, HttpServletRequest request) {
        LoginResponseDto response = userAuthService.verifyOtp(verifyOtpRequest.getUsername(), verifyOtpRequest.getOtp(), request);
        return ApiResponseUtils.response(HttpStatus.OK, response,"verification success");
    }
}
