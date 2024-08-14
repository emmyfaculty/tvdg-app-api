package com.tvdgapp.dtos.qutoe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteRequestDto {
    private String subject;
    private String description;
    private String companyName;
    private String industry;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String country;
    private String state;
    private String city;
    private String zipcode;
    private String streetAddress;
//    private ServiceOption serviceOption;
}

