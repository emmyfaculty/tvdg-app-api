package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostalAddress {
    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("countryCode")
    private String countryCode;
}
