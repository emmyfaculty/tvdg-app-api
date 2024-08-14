package com.tvdgapp.dtos.shipment.pricingcaculation;

import lombok.Data;

@Data
public class PickupStateDTO {
    private Long id;
    private String stateName;
    private String pickupRegion;
}
