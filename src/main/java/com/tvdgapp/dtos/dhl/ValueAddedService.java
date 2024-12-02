package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class ValueAddedService {
    private String serviceCode;
    private double value;
    private String currency;
}