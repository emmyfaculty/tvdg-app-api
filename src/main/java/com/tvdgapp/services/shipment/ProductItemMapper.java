package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.ProductItemDto;
import com.tvdgapp.models.shipment.ProductItem;

public class ProductItemMapper {

    public static ProductItem toEntity(ProductItemDto dto) {
        ProductItem entity = new ProductItem();
        entity.setDescription(dto.getDescription());
        entity.setQuantity(dto.getQuantity());
        entity.setUnits(dto.getUnits());
        entity.setValue(dto.getValue());
        entity.setWeight(dto.getWeight());
        entity.setManufacturingCountry(dto.getManufacturingCountry());
        return entity;
    }
}

