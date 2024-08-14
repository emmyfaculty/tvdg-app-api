package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.*;
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
public class ShippingRateRequestDto {

    private SenderDetailsDto senderDetails;
    private ReceiverDetailsDto receiverDetails;
    private int totalNumberOfPackages;
    private BigDecimal totalShipmentValue;
    private Dimension dimension;
    private Long shippingOptionId;
    private double totalShipmentWeight;
    private List<ProductItemDto> productItems;
    private boolean useOurPackaging;
    private boolean insurance;
    private String referralCode;
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private String shippedDate;
    private Long customerUserId;
    private Integer serviceId;
    private Units units;
    private Pickup pickup;
    private BigDecimal pickupFee;
}
