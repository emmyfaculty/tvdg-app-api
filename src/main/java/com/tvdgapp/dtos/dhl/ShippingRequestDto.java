package com.tvdgapp.dtos.dhl;

import lombok.Data;

import java.util.List;

@Data
public class ShippingRequestDto {
    private String plannedShippingDateAndTime;
    private String productCode;
    private List<Account> accounts;
    private Pickup pickup;
    private OutputImageProperties outputImageProperties;
    private CustomerDetails customerDetails;
    private Content content;
    private List<ValueAddedService> valueAddedServices;
    private List<CustomerReference> customerReferences;

    // Getters and setters
}
