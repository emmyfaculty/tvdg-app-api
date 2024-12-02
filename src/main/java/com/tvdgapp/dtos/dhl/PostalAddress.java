package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class PostalAddress {
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String postalCode;
    private String cityName;
    private String countyName;
    private String countryCode;
}
