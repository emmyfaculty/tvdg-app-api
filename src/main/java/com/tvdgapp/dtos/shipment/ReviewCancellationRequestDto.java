package com.tvdgapp.dtos.shipment;

import lombok.Data;

@Data
public class ReviewCancellationRequestDto {
    private boolean approved; // True if approved, false if rejected
    private String remarks; // Admin remarks if rejected
}
