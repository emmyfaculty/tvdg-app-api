package com.tvdgapp.dtos.affiliate;

import lombok.Data;

import java.util.Date;

@Data
public class AffiliateResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    protected String dateOfBirth;
    private String identificationCard;
    private String identificationNumber;
    private String streetAddress;
    private String country;
    private String state;
    private String city;
    private String username;
    private String referralCode;
}
