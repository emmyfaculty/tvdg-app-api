package com.tvdgapp.dtos.shipment;

import lombok.Data;

@Data
public class PaginationRequest {
    private int start;
    private int length;
}
