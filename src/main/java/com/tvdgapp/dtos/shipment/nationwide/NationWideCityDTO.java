package com.tvdgapp.dtos.shipment.nationwide;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NationWideCityDTO {

    private Long id;

    @NotNull(message = "City name is required")
    private String name;

    @NotNull(message = "State ID is required")
    private Long stateId;

    private String description;
}
