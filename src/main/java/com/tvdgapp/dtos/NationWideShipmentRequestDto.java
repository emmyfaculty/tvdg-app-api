package com.tvdgapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.dtos.shipment.ProductItemDto;
import com.tvdgapp.dtos.shipment.ReceiverDetailsDto;
import com.tvdgapp.dtos.shipment.SenderDetailsDto;
import com.tvdgapp.dtos.shipment.Units;
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
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class NationWideShipmentRequestDto {

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
