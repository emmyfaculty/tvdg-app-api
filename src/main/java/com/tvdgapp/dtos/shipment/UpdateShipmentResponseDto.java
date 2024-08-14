package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class UpdateShipmentResponseDto {

    private CustomerType customerType;
    private ShipmentType shipmentType;
    private ServiceType serviceType;
    private SenderDetailsDto senderDetails;
    private ReceiverDetailsDto receiverDetails;
    private int totalNumberOfPackages;
    private double totalShipmentValue;
    private DimensionDto dimension;
    private String shippingOption;
    private double totalShipmentWeight;
    private Set<ProductItemDto> productItems;
    private double packagingFee;
    private double volumetricWeight;
    private double purchaseHigherLiability;
    private double totalShipmentAmount;
    private boolean useOurPackaging;
    private String referralCode;
    private String trackingNumber;
    private double insuranceFee;
    private double vat;
    private Long customerId;
    private String status;

}
