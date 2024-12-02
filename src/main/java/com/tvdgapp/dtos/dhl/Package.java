package com.tvdgapp.dtos.dhl;

import lombok.Data;

@Data
class Package {
    private double weight;
    private Dimensions dimensions;
    private String description;
}
