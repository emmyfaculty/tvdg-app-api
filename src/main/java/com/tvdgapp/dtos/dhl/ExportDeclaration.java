package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class ExportDeclaration {
    private List<LineItem> lineItems;
    private String exportReason;
    private List<AdditionalCharge> additionalCharges;
    private Invoice invoice;
    private String placeOfIncoterm;
    private String exportReasonType;
    private String shipmentType;
}
