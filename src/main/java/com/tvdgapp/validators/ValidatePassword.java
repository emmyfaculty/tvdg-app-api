package com.tvdgapp.validators;

import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.exceptions.InvalidRequestException;

public class ValidatePassword {
    public static void validateSamePassword(ChangePasswordDto changePasswordDto) {
        ValidationErrors validationErrors = new ValidationErrors();
        if (changePasswordDto.getNewPassword() == null || changePasswordDto.getConfirmPassword() == null) {
            throw new IllegalArgumentException("New password and confirm password must not be null");
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        if (validationErrors.hasErrors()) {
            throw new InvalidRequestException(validationErrors);
        }
    }
}
