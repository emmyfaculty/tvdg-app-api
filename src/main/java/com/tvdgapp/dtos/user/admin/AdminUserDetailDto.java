package com.tvdgapp.dtos.user.admin;

import lombok.Data;

@Data
public class AdminUserDetailDto extends AdminUserRequestDto{
    private Long userId;
    private String accessCode;
}
