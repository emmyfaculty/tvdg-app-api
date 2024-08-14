package com.tvdgapp.dtos.shipment;

import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.ShipmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ShippingOptionDto {
    @NotBlank
    private String serviceName;
    @Positive
    private double servicePrice;
    private Long deliveryDate;
    private String carrier;
    private String pricingCategory;
    private ShipmentType shipmentType;
    private CustomerType customerType;
    private ServiceType serviceType;
}