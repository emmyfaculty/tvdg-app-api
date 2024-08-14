package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.pricingcaculation.PickupStateDTO;
import com.tvdgapp.models.shipment.pricingcaculation.PickupState;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PickupStateMapper {
    PickupStateMapper INSTANCE = Mappers.getMapper(PickupStateMapper.class);

    PickupStateDTO toPickupStateDTO(PickupState pickupState);

    PickupState toPickupState(PickupStateDTO pickupStateDTO);
}
