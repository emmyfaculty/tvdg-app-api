package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.pricingcaculation.ExpectedDeliveryDayDTO;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExpectedDeliveryDayMapper {
    ExpectedDeliveryDayMapper INSTANCE = Mappers.getMapper(ExpectedDeliveryDayMapper.class);

    @Mapping(source = "shippingService.serviceId", target = "serviceId")
    ExpectedDeliveryDayDTO toDTO(ExpectedDeliveryDay expectedDeliveryDay);

    @Mapping(source = "serviceId", target = "shippingService.serviceId")
    ExpectedDeliveryDay toEntity(ExpectedDeliveryDayDTO expectedDeliveryDayDTO);
}
