package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class CustomerReference {
    private String value;
    private String typeCode;

}