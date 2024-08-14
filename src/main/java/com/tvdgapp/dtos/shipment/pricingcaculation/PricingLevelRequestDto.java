package com.tvdgapp.dtos.shipment.pricingcaculation;

import lombok.*;

import java.util.List;

@Data
public class PricingLevelRequestDto {
    private String levelName;
    private String description;
    private List<Integer> priceModelLevelIds;
}
