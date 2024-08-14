package com.tvdgapp.dtos.shipment.nationwide;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNationWideRegionDto {
    @NotNull(message = "Name is required")
    private String name;
    private String description;
}