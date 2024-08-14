package com.tvdgapp.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.wallet.ShipmentPayment;
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
public class ListShipmentDto {

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
    private String servicePortfolio;
    private String shipmentStatus;
    private String paymentStatus;
    private BigDecimal totalProductItemsValue;
    private double totalProductItemsWeight;
    private BigDecimal totalPackageCategoryAmount;
    private String carrier;
    private VerificationStatus verificationStatus;
    private String verifiedBy;
    private AuditSectionDto auditSection;
    private Long shipmentCount;
    private List<ShipmentPayment> shipmentPayments;
    @Temporal(TemporalType.DATE)
    private String shippedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private String orderDate;
    private String paymentMethod;
    private String estimatedDeliveryDate;
    private BigDecimal transportCharge;
    private Long customerId;






}
