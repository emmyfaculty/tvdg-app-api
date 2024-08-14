//package com.tvdgapp.controllers.auth.customeruser;
//
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.auth.ForgotPasswordRequestDto;
//import com.tvdgapp.dtos.auth.LoginRequest;
//import com.tvdgapp.dtos.auth.LoginResponseDto;
//import com.tvdgapp.dtos.auth.ResetPasswordDto;
//import com.tvdgapp.services.AuthService;
//import com.tvdgapp.utils.ApiResponseUtils;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
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
//@RequestMapping(value = "/api/v1/customer")
//public class CustomerUserAuthController {
//
//private final AuthService customerAuthService;
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiDataResponse<LoginResponseDto>> login(@RequestBody LoginRequest loginRequest) {
//        LoginResponseDto loginResponseDto = this.customerAuthService.login(loginRequest.getUserName(),loginRequest.getPassword());
//        return ApiResponseUtils.response(HttpStatus.OK, loginResponseDto, "Login successfully");
//
//    }
////
//    @PostMapping("/forgotpassword")
//    public ResponseEntity<ApiDataResponse<Object>> initiateForgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
//
//            customerAuthService.publishForgotPasswordEmail(forgotPasswordRequestDto.getEmail());
//        return ApiResponseUtils.response(HttpStatus.OK, "Forgot password initiated successfully");
//    }
//
//    @PostMapping("/resetpassword")
//    public ResponseEntity<ApiDataResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
//            customerAuthService.resetPassword(resetPasswordDto);
//        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
//    }
////}
