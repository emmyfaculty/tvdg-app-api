package com.tvdgapp.dtos.shipment.pricingcaculation;

import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.pricingcaculation.Carrier;
import lombok.Data;

@Data
public class ShippingServiceResponseDto {
    private Integer serviceId;
    private String serviceName;
    private ServiceType type;
    private Carrier carrier;
    private String description;

    // Getters and Setters
}
