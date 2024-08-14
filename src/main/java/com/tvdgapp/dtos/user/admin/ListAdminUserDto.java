package com.tvdgapp.dtos.user.admin;


import com.tvdgapp.dtos.user.ListUserDto;
import com.tvdgapp.models.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ListAdminUserDto extends ListUserDto {

    public ListAdminUserDto(Long id, String name, String email, String phone, UserStatus status, Date dateAdded) {
        super(id, name,email,phone,status,dateAdded);
    }
}



