package com.tvdgapp.dtos.shipment;

import com.tvdgapp.models.shipment.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePortfolioResponseDto {
    private Integer id;
    private String serviceName;
    private BigDecimal totalCost;
    private String deliveryDate;
    private ShipmentType shipmentType;
    private Integer pricingLevelId;
}
