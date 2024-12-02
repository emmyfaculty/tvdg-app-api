package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShipperReference {
    @JsonProperty("referenceType")
    private String referenceType;

    @JsonProperty("referenceValue")
    private String referenceValue;

}