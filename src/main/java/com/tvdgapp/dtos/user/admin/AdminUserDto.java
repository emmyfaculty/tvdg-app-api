package com.tvdgapp.dtos.user.admin;

import com.tvdgapp.dtos.user.UserDto;
import com.tvdgapp.models.user.admin.GenerateCode;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class AdminUserDto extends UserDto {

    private GenerateCode generateCode;
}
