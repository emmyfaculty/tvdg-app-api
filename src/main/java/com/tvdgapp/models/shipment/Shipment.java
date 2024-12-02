package com.tvdgapp.models.shipment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tvdgapp.dtos.shipment.Units;
import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.utils.CodeGeneratorUtils;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.tvdgapp.constants.SchemaConstant.TABLE_SHIPMENT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name=TABLE_SHIPMENT)
@SuppressWarnings("NullAway.Init")
public class Shipment extends TvdgAppEntity<Long, Shipment> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "shipment_ref", unique = true, nullable = false, length = 120, columnDefinition = "VARCHAR(120) COLLATE utf8mb4_unicode_ci")
    private String shipmentRef;

    @Column(unique = true)
    private String trackingNumber;

    private int totalNumberOfPackages;
    private BigDecimal totalShipmentValue;
    private BigDecimal transportCharge;

    @Embedded
    private Dimension dimension;

    private double totalShipmentWeight;
    private double volumetricWeight;
    @Enumerated(EnumType.STRING)
    private Units units;


    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductItem> productItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "shipping_service_id")
    private ShippingService shippingService;

//    private BigDecimal insurance;
    private BigDecimal packagingFee;
    private BigDecimal insuranceFee;
    private String paymentProof;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_user_id")
//    @JsonManagedReference
//    private CustomerUser customerUser;

    @Nullable
    @Column(name = "shipment_status")
    private String status;
    @Enumerated(EnumType.STRING)
    @Nullable
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Nullable
    private PaymentMethod paymentMethod;

    private BigDecimal totalShipmentAmount;
    private String referralCode;
    private BigDecimal vat;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDate shippedDate;
    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;


    private Integer pricingLevelId;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pickup pickup;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal pickupFee = BigDecimal.ZERO;
    @Column(name = "expected_delivery_date")
    private Date expectedDeliveryDate;
    @Column(precision = 19, scale = 2)
    private BigDecimal documentCharge = BigDecimal.ZERO;;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    public void addProductItem(ProductItem productItem) {
        productItems.add(productItem);
        productItem.setShipment(this);
    }

    public void removeProductItem(ProductItem productItem) {
        productItems.remove(productItem);
        productItem.setShipment(null);
    }

    @PrePersist
    public void generateShipmentRef() {
        if (shipmentRef == null) {
            shipmentRef = CodeGeneratorUtils.generateUniqueShipmentRef();
        }
    }
}

