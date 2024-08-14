package com.tvdgapp.dtos.shipment;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<ListShipmentDto> data;
    private int recordsTotal;
    private int recordsFiltered;
}
