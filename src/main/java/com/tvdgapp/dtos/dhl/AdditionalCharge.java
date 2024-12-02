package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class AdditionalCharge {
    private double value;
    private String typeCode;
}