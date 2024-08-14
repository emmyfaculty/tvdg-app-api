package com.tvdgapp.mapper;



import com.tvdgapp.dtos.shipment.pricingcaculation.PickupLocationDTO;
import com.tvdgapp.models.shipment.pricingcaculation.PickupLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PickupLocationMapper {

    PickupLocationDTO toDto(PickupLocation pickupLocation);
    PickupLocation toEntity(PickupLocationDTO pickupLocationDTO);
}
