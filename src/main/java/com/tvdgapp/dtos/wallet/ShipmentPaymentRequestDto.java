package com.tvdgapp.dtos.wallet;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShipmentPaymentRequestDto {
    @NotNull(message = "Shipment ID is required")
    @Size(min = 1, message = "Shipment ID must not be empty")
    private Long shipmentId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be greater than 0")
    private BigDecimal amount;

    // Getters and Setters
}
