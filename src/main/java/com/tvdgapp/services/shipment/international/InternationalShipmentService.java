package com.tvdgapp.services.shipment.international;

import com.tvdgapp.dtos.shipment.CalculateShippingOptionDto;
import com.tvdgapp.dtos.shipment.ServicePortfolioResponseDto;

import java.util.List;

public interface InternationalShipmentService {
    List<ServicePortfolioResponseDto> calculateServiceOptions(CalculateShippingOptionDto calculateShippingOptionDto);
//    Shipment createShipment(ShipmentRequestDto shipmentRequestDto);
}
