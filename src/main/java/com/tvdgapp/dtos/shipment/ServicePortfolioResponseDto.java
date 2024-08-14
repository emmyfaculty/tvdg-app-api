package com.tvdgapp.dtos.shipment;

import com.tvdgapp.models.shipment.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePortfolioResponseDto {
    private Integer id;
    private String serviceName;
    private double totalCost;
    private String deliveryDate;
    private ShipmentType shipmentType;
    private Integer pricingLevelId;
}
