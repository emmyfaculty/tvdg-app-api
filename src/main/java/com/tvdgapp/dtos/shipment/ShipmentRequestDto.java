package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class ShipmentRequestDto {

    private SenderDetailsDto senderDetails;
    private ReceiverDetailsDto receiverDetails;
    private int totalNumberOfPackages;
    private BigDecimal totalShipmentValue;
    private Dimension dimension;
    private Integer serviceId;
    private double totalShipmentWeight;
    @Enumerated(EnumType.STRING)
    private Units units;

    private List<ProductItemDto> productItems;
//    private double packagingFee;
//    private double purchaseHigherLiability;
//    private double totalShipmentAmount;
    private boolean useOurPackaging;
    private boolean insurance;
    private String referralCode;
//    private String trackingNumber;
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private String shippedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private String orderDate;
    private PaymentMethod paymentMethod;
    private Pickup pickup;
    private BigDecimal pickupFee;

}
