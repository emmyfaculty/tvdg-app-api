package com.tvdgapp.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.common.ListingDto;
import com.tvdgapp.models.user.UserStatus;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ListUserDto extends ListingDto {

    private String title;
    private String status;
    @JsonFormat(pattern = DateConstants.DATE_TIME_DISPLAY_FORMAT)
    private Date dateAdded;
    private String name;
    private String email;
    private String phone;
    private String streetAddress;
    private String country;
    private String state;
    private String city;
    public ListUserDto(Long id,String title, String name, String email, String phone, UserStatus status, Date dateAdded, String streetAddress, String country, String state, String city) {
        super(id, name);
        this.title=title;
        this.status = status.name();
        this.dateAdded = dateAdded;
        this.email=email;
        this.name=name;
        this.phone=phone;
        this.streetAddress = streetAddress;
        this.country = country;
        this.state = state;
        this.city=city;

    }

    public ListUserDto(Long id,String title, String name, String email, String phone, UserStatus status, Date dateAdded) {
        super(id, name);
        this.title = title;
        this.status = status.name();
        this.dateAdded = dateAdded;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
    public ListUserDto(Long id, String name, String email, String phone, UserStatus status, Date dateAdded) {
        super(id, name);
        this.status = status.name();
        this.dateAdded = dateAdded;
        this.email=email;
        this.name=name;
        this.phone=phone;
    }
}