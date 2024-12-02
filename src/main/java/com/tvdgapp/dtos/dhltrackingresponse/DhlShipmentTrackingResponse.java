package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class DhlShipmentTrackingResponse {
    @JsonProperty("shipments")
    private List<Shipment> shipments;
}
