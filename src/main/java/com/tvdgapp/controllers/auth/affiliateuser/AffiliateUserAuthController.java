//package com.tvdgapp.controllers.auth.affiliateuser;
//
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.auth.LoginRequest;
//import com.tvdgapp.dtos.auth.LoginResponseDto;
//import com.tvdgapp.services.AuthService;
//import com.tvdgapp.utils.ApiResponseUtils;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping(value = "/api/v1/affiliate")
//public class AffiliateUserAuthController {
//
////    @Qualifier("affiliateAuthService")
//    private final AuthService affiliateAuthService;
//
//    @PostMapping("/login")
//    @ApiResponses(value = {@ApiResponse(code = 401, message = "not authorized!")})
//    public ResponseEntity<ApiDataResponse<LoginResponseDto>> login(@RequestBody LoginRequest loginRequest) {
//        LoginResponseDto loginResponseDto = this.affiliateAuthService.login(loginRequest.getUserName(),loginRequest.getPassword());
//        return ApiResponseUtils.response(HttpStatus.OK, loginResponseDto, "Login successfully");
//
//    }
//
//
////    @PostMapping("/forgotpassword")
////    public ResponseEntity<ApiDataResponse<Object>> initiateForgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
////
////            affiliateAuthService.publishForgotPasswordEmail(forgotPasswordRequestDto.getEmail());
////        return ApiResponseUtils.response(HttpStatus.OK, "Forgot password initiated successfully");
////    }
////
////    @PostMapping("/resetpassword")
////    public ResponseEntity<ApiDataResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
////            affiliateAuthService.resetPassword(resetPasswordDto);
////        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
////    }
//}
