package com.tvdgapp.dtos.shipment;

import lombok.Data;

@Data
public class CancellationRequestDto {
    private String shipmentRef;
    private String reason; // Reason for cancellation
}