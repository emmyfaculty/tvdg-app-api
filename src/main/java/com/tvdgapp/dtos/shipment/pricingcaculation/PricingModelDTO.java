package com.tvdgapp.dtos.shipment.pricingcaculation;

import com.tvdgapp.models.shipment.pricingcaculation.PriceCategory;
import lombok.Data;

@Data
public class PricingModelDTO {
    private Long id;
    private String levelName;
    private String description;

}
