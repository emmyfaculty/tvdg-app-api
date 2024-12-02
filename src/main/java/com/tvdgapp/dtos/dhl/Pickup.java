package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Pickup {
    @JsonProperty("isRequested")
    private boolean isRequested;
}