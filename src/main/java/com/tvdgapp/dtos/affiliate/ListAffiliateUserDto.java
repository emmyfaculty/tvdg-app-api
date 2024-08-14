package com.tvdgapp.dtos.affiliate;

import com.tvdgapp.dtos.user.ListUserDto;
import com.tvdgapp.models.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ListAffiliateUserDto extends ListUserDto {

    public ListAffiliateUserDto(Long id, String title, String name, String email, String phone, UserStatus status, Date dateAdded, String streetAddress, String country, String state, String city ) {
        super(id, title, name,email,phone,status,dateAdded, streetAddress, country, state, city);
    }
}