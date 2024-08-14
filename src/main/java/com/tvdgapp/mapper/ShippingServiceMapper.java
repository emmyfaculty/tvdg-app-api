package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.pricingcaculation.CreateShippingServiceDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.ShippingServiceResponseDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.UpdateShippingServiceDto;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;

public class ShippingServiceMapper {

    public static ShippingService toEntity(CreateShippingServiceDto dto) {
        ShippingService shippingService = new ShippingService();
        shippingService.setServiceName(dto.getServiceName());
        shippingService.setType(dto.getType());
        shippingService.setCarrier(dto.getCarrier());
        shippingService.setDescription(dto.getDescription());
        return shippingService;
    }

    public static ShippingService toEntity(UpdateShippingServiceDto dto) {
        ShippingService shippingService = new ShippingService();
        shippingService.setServiceId(dto.getServiceId());
        shippingService.setServiceName(dto.getServiceName());
        shippingService.setType(dto.getType());
        shippingService.setCarrier(dto.getCarrier());
        shippingService.setDescription(dto.getDescription());
        return shippingService;
    }

    public static ShippingServiceResponseDto toDto(ShippingService shippingService) {
        ShippingServiceResponseDto dto = new ShippingServiceResponseDto();
        dto.setServiceId(shippingService.getServiceId());
        dto.setServiceName(shippingService.getServiceName());
        dto.setType(shippingService.getType());
        dto.setCarrier(shippingService.getCarrier());
        dto.setDescription(shippingService.getDescription());
        return dto;
    }
}
