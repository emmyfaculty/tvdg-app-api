package com.tvdgapp.dtos.shipment.pricingcaculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricingLevelResponseDto {
    private Integer levelId;
    private String levelName;
    private String description;
//    private List<Long> priceModelLevelIds;
}
