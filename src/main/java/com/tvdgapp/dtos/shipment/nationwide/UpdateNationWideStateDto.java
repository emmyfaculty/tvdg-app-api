package com.tvdgapp.dtos.shipment.nationwide;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNationWideStateDto {
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Region ID is required")
    private Long regionId;
    private String description;
}