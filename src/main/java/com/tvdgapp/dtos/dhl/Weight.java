package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Weight {
    private double netValue;
    private double grossValue;
}
