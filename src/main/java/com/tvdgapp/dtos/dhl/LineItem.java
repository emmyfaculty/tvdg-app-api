package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class LineItem {
    private int number;
    private Quantity quantity;
    private double price;
    private String description;
    private Weight weight;
    private List<CommodityCode> commodityCodes;
    private String exportReasonType;
    private String manufacturerCountry;
}
