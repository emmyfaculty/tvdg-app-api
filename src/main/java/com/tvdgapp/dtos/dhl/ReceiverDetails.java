package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class ReceiverDetails {
    private PostalAddress postalAddress;
    private ContactInformation contactInformation;
    private String typeCode;
}
