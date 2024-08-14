package com.tvdgapp.dtos.shipment.pricingcaculation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PickupLocationDTO {

    private Long id;
    private Double weightBandStart;
    private Double weightBandEnd;
    private String pickupRegion;
    private BigDecimal pickupFee;

}
