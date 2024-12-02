package com.tvdgapp.dtos.dhltrackingresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Event {
    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private String time;

    @JsonProperty("typeCode")
    private String typeCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("serviceArea")
    private List<ServiceArea> serviceArea;

    @JsonProperty("signedBy")
    private String signedBy;
}
