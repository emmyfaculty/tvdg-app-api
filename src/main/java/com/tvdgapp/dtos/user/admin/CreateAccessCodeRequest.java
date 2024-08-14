package com.tvdgapp.dtos.user.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// CreateAccessCodeRequest.java
@Data
public class CreateAccessCodeRequest {
    @NotNull(message = "User ID is required")
    private Long userId;//
    // private Long createdBy;

}