package com.tvdgapp.dtos;

import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.wallet.ShipmentPayment;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class NationWideShipmentResponseDto {
    private Long id;
    private String trackingNumber;
//    private CustomerType customerType;
//    private ShipmentType shipmentType;
//    private ServiceType serviceType;
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
    private String serviceOption;
    private String shipmentStatus;
    private Long customerId;
    private BigDecimal insurance;
    private BigDecimal vat;
    @Temporal(TemporalType.DATE)
    private String shippedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private String orderDate;
    private String paymentMethod;
    private String estimatedDeliveryDate;
    private BigDecimal transportCharge;
    //    private double packageCategoryAmount;
    private AuditSectionDto auditSection;
    private Long shipmentCount;
    private String paymentStatus;

    private VerificationStatus verificationStatus;
    private Long verifiedBy;
    private List<ShipmentPayment> shipmentPayments;
    private BigDecimal totalProductItemsValue;
    private double totalProductItemsWeight;
    private BigDecimal totalPackageCategoryAmount;
    private String carrier;
}
