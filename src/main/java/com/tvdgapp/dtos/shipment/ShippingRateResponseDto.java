package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.Dimension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class ShippingRateResponseDto {
    private String sendersCompanyName;
    private String receiversCompanyName;
    private String sendersCountry;
    private String receiversCountry;
    private String sendersAddress;
    private String receiversAddress;
    private String sendersCity;
    private String receiversCity;
    private String sendersZipcode;
    private String receiversZipcode;
    private int totalNumberOfPackages;
    private double totalShipmentWeight;
    private double volumetricWeight;
    private double totalShipmentValue;
    private Dimension dimension;
    private Long customerUserId;
}
