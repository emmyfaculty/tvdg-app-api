package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Piece {
    @JsonProperty("number")
    private int number;

    @JsonProperty("typeCode")
    private String typeCode;

    @JsonProperty("shipmentTrackingNumber")
    private String shipmentTrackingNumber;

    @JsonProperty("trackingNumber")
    private String trackingNumber;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("actualWeight")
    private double actualWeight;

    @JsonProperty("dimensions")
    private Dimensions dimensions;

    @JsonProperty("unitOfMeasurements")
    private String unitOfMeasurements;

    @JsonProperty("events")
    private List<Event> events;
}
