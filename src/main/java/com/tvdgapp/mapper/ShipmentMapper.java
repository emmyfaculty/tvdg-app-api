package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.ShipmentDto;
import com.tvdgapp.models.shipment.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    ShipmentDto toShipmentDTO(Shipment shipment);
    Shipment toShipment(ShipmentDto shipmentDTO);

//    @Mapping(source = "id", target = "shipmentId")
//    @Mapping(source = "shipmentRef", target = "shipmentRef")
//        // Add other mappings as needed
    ShipmentDto toDto(Shipment shipment);

//    @Mapping(source = "shipmentId", target = "id")
//    @Mapping(source = "shipmentRef", target = "shipmentRef")
        // Add other mappings as needed
    Shipment toEntity(ShipmentDto shipmentDto);
}