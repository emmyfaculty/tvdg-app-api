package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Shipment {
    @JsonProperty("shipmentTrackingNumber")
    private String shipmentTrackingNumber;

    @JsonProperty("status")
    private String status;

    @JsonProperty("shipmentTimestamp")
    private String shipmentTimestamp;

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("shipperDetails")
    private ShipperDetails shipperDetails;

    @JsonProperty("receiverDetails")
    private ReceiverDetails receiverDetails;

    @JsonProperty("totalWeight")
    private double totalWeight;

    @JsonProperty("unitOfMeasurements")
    private String unitOfMeasurements;

    @JsonProperty("shipperReferences")
    private List<ShipperReference> shipperReferences;

    @JsonProperty("events")
    private List<Event> events;

    @JsonProperty("numberOfPieces")
    private int numberOfPieces;

    @JsonProperty("pieces")
    private List<Piece> pieces;

}
