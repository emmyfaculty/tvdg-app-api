package com.tvdgapp.mapper;

import com.tvdgapp.dtos.rider.RiderShipmentMapDto;
import com.tvdgapp.models.user.rider.RiderShipmentMap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RiderShipmentMapMapper {

    RiderShipmentMapMapper INSTANCE = Mappers.getMapper(RiderShipmentMapMapper.class);

    @Mapping(source = "rider.id", target = "riderId")
    @Mapping(source = "shipment.shipmentRef", target = "shipmentRef")
    @Mapping(source = "assignedById", target = "assignedById")
    RiderShipmentMapDto toDto(RiderShipmentMap entity);

    @Mapping(source = "riderId", target = "rider.id")
    @Mapping(source = "shipmentRef", target = "shipment.shipmentRef")
    RiderShipmentMap toEntity(RiderShipmentMapDto dto);
}
