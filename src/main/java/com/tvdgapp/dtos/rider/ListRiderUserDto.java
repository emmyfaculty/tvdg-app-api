package com.tvdgapp.dtos.rider;

import com.tvdgapp.dtos.user.ListUserDto;
import com.tvdgapp.models.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ListRiderUserDto extends ListUserDto {

    public ListRiderUserDto(Long id, String title, String name, String email, String phone, UserStatus status, Date dateAdded) {
        super(id, title, name,email,phone,status,dateAdded);
    }
}