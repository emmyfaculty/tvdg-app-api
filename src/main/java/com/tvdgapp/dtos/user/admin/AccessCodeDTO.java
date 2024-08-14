package com.tvdgapp.dtos.user.admin;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

// AccessCodeDTO.java
@Data
public class AccessCodeDTO {

    private Long userId;
    private String accessCode;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer tsCreated;
    private Integer tsModified;

}