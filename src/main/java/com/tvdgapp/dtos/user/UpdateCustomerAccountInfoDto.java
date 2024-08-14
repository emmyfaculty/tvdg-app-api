package com.tvdgapp.dtos.user;

import lombok.Data;

@Data
public class UpdateCustomerAccountInfoDto {
    private String companyName;
    private String contactName;
    private String companyRegistrationNumber;
    private String companyEmail;
    private String phoneNumber;
    private String natureOfBusiness;
    private String industry;
    private String postalCode;
    private String city;
    private String state;
    private String streetAddress;
}
