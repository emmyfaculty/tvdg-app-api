package com.tvdgapp.dtos.shipment.pricingcaculation;

import lombok.Data;

@Data
public class ExpectedDeliveryDayDTO {
    private Long id;
//    private String country;
    private Long regionId;
    private String dayRange;
    private Integer serviceId; // Use the ID for the relationship
}
