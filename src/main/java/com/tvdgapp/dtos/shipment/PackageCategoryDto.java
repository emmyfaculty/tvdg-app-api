package com.tvdgapp.dtos.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageCategoryDto {
    private String categoryName;
    private double categoryDocumentAmount;
}
