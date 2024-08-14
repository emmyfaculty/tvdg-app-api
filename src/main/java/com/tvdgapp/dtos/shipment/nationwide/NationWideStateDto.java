package com.tvdgapp.dtos.shipment.nationwide;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NationWideStateDto {
    private Long id;
    @NotNull(message = "Name is required")
    private String name;
    private Integer regionId;
    private String description;
}

