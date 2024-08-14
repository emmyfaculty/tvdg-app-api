package com.tvdgapp.dtos.shipment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverDetailsDto {
    private String shipmentRef;
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String companyName;
    private String tertiaryCountry;

    @NotBlank
    private String country;

    @NotBlank
    private String address;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    @Email
    private String email;

    @NotBlank
    private String phoneNo;
}
