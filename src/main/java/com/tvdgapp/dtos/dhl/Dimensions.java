package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Dimensions {
    private double length;
    private double width;
    private double height;
}