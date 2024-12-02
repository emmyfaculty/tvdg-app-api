package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class ImageOption {
    private String templateName;

    @JsonProperty("isRequested")
    private Boolean isRequested;

    private Boolean hideAccountNumber; // For "label"
    private String typeCode;

    // Fields conditionally required based on typeCode
    private String invoiceType; // Required when typeCode is "invoice"
    private String languageCode; // Required when typeCode is "invoice" or "waybillDoc"
    private String encodingFormat; // Required for all types

    // Additional fields for various types
    private Integer numberOfCopies; // For "label"
    private String languageScriptCode; // Required for all types
    private String shipmentReceiptCustomerDataText; // Required for all types
    private Boolean renderDHLLogo; // For "waybillDoc"
    private Boolean fitLabelsToA4; // For "waybillDoc"
    private String labelFreeText; // For "waybillDoc"
    private String labelCustomerDataText; // For "waybillDoc"
}
