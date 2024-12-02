package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Invoice {
    private String number;
    private String date;
}