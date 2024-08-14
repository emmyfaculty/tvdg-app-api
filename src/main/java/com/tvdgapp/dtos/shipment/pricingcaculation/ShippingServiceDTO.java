package com.tvdgapp.dtos.shipment.pricingcaculation;

import com.tvdgapp.models.shipment.ServiceType;
import lombok.Data;

import java.util.List;

@Data
public class ShippingServiceDTO {
    private Integer id;
    private String serviceName;
    private ServiceType serviceType;
    private Long priceModelLevelId;
    private String carrier;
    private List<Long> expectedDeliveryDays;
    private List<Long> priceModelLevels;

}

