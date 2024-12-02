package com.tvdgapp.dtos.shipment;

import lombok.Data;

@Data
public class ShipmentStatusUpdateRequestDto {
    private String status;
    private String location;
    private String comments;
}
