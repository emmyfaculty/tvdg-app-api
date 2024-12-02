package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ShipperDetails {
    @JsonProperty("name")
    private String name;

    @JsonProperty("postalAddress")
    private PostalAddress postalAddress;

    @JsonProperty("serviceArea")
    private List<ServiceArea> serviceArea;
}
