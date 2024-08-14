package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.ProductItemDto;
import com.tvdgapp.models.shipment.ProductItem;

public class ProductItemMapper {

    public static ProductItem toEntity(ProductItemDto dto) {
        ProductItem productItem = new ProductItem();
//        productItem.setProductName(dto.getName());
        productItem.setDescription(dto.getDescription());
        productItem.setWeight(dto.getWeight());
        productItem.setValue(dto.getValue());
        productItem.setUnits(dto.getUnits());
        productItem.setQuantity(dto.getQuantity());
        productItem.setManufacturingCountry(dto.getManufacturingCountry());
        return productItem;
    }

    public static ProductItemDto toDTO(ProductItem productItem) {
        ProductItemDto dto = new ProductItemDto();
//        dto.setName(productItem.getProductName());
        dto.setDescription(productItem.getDescription());
        dto.setWeight(productItem.getWeight());
        dto.setValue(productItem.getValue());
        dto.setUnits(productItem.getUnits());
        dto.setQuantity(productItem.getQuantity());
        dto.setManufacturingCountry(productItem.getManufacturingCountry());
        return dto;
    }
}
