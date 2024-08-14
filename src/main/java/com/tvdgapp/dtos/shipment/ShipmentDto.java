package com.tvdgapp.dtos.shipment;


import com.tvdgapp.models.shipment.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ShipmentDto {
    private Long id;
    private String trackingNumber;
    private CustomerType customerType;
    private ShipmentType shipmentType;
    private ServiceType serviceType;
    private SenderDetailsDto senderDetails;
    private ReceiverDetailsDto receiverDetails;
    private int totalNumberOfPackages;
    private BigDecimal totalShipmentValue;
    private double totalShipmentWeight;
    private double volumetricWeight;
    private String shippingOption;
    private BigDecimal insurance;
    private BigDecimal packagingFee;
    private BigDecimal insuranceFee;
    private String paymentProof;
    private String shipmentStatus;
    private String paymentMethod;
    private BigDecimal totalShipmentAmount;
    private String referralCode;
    private BigDecimal vat;
    private Set<ProductItemDto> productItems;

}
