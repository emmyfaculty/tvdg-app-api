package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class ShipmentDetailDto {

    private Long id;
    private String trackingNumber;
    private CustomerType customerType;
    private ShipmentType shipmentType;
    private ServiceType serviceType;
    private SenderDetailsDto senderDetails;
    private ReceiverDetailsDto receiverDetails;
    private int totalNumberOfPackages;
    private BigDecimal totalShipmentValue;
    private DimensionDto dimension;
    private double totalShipmentWeight;
    private double volumetricWeight;
    private BigDecimal packagingFee;
    private BigDecimal totalShipmentAmount;
    private Set<ProductItemDto> productItems;
    private String referralCode;
    private String shippingOption;
    private String status;
    private String carrier;
    private String shippedDate;

}
