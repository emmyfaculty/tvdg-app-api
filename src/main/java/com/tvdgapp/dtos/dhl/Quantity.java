package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Quantity {
    private String unitOfMeasurement;
    private int value;
}
