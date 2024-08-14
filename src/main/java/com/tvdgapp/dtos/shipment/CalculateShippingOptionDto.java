package com.tvdgapp.dtos.shipment;

import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.models.shipment.Dimension;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.ShipmentType;
import com.tvdgapp.models.user.customer.CustomerUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateShippingOptionDto {
    private Dimension dimension;
    private double totalShipmentWeight;
    private Units units;
    private ShipmentType shipmentType;
    private ServiceType serviceType;
    private String country;
}
