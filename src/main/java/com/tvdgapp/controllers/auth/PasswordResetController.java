package com.tvdgapp.controllers.auth;


import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.auth.ForgotPasswordRequestDto;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.services.user.UserService;
import com.tvdgapp.utils.ApiResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "${api.basepath-api}")
public class PasswordResetController {

    private final UserService userService;
    @PostMapping("/forgotpassword")
    public ResponseEntity<ApiDataResponse<Object>> initiateForgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) throws Exception {
            userService.publishForgotPasswordEmail(forgotPasswordRequestDto.getEmail(), forgotPasswordRequestDto.getUrl());
        return ApiResponseUtils.response(HttpStatus.OK, "Forgot password initiated successfully");
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<ApiDataResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) throws Exception {
            userService.resetPassword(resetPasswordDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
    }

}
