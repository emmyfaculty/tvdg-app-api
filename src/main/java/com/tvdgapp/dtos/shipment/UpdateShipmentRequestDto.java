package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class UpdateShipmentRequestDto {

    private SenderDetailsDto senderDetails;
    private ReceiverDetailsDto receiverDetails;
    private int totalNumberOfPackages;
    private BigDecimal totalShipmentValue;
    private Dimension dimension;
    private Long shippingOptionId;
    private double totalShipmentWeight;
    private Set<ProductItemDto> productItems;
    private BigDecimal packagingFee;
    private double volumetricWeight;
    private BigDecimal purchaseHigherLiability;
    private BigDecimal totalShipmentAmount;
    private boolean useOurPackaging;
    private String referralCode;
    private String trackingNumber;
    private BigDecimal insuranceFee;
    private BigDecimal vat;
    private Long customerId;
    private String status;
    private boolean insurance;
    private Integer servicePortfolioId;
    private Units units;
    private Pickup pickup;
    private BigDecimal pickupFee;

}
