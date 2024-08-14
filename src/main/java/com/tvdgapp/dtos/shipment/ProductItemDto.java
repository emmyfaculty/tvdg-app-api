package com.tvdgapp.dtos.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItemDto {
    private String PackageCategoryName;
    private BigDecimal packageCategoryAmount;
    private String description;
    private int quantity;
    private String units;
    private BigDecimal value;
    private double weight;
    private String shipmentRef;
    private String manufacturingCountry;
}
