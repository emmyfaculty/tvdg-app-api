package com.tvdgapp.models.shipment.pricingcaculation;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.reference.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.tvdgapp.constants.SchemaConstant.TABLE_INTERNATIONAL_SHIPPING_PRICING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name= TABLE_INTERNATIONAL_SHIPPING_PRICING)
//@ValidWeightBand
public class PriceModelLevel /*extends TvdgAppEntity<Long, PriceModelLevel> implements Auditable */ {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull
//    @DecimalMin(value = "0.0", message = "Weight band start must be greater than or equal to 0")
//    private BigDecimal weightBandStart;
//
//    @DecimalMin(value = "0.0", inclusive = false, message = "Weight band end must be greater than 0")
//    private BigDecimal weightBandEnd;
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private Units units;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "region_id")
//    private Region region;
//
//    @NotNull
//    private String description;
//
//    @NotNull
//    private double price;
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private Currency currency;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "service_id")
//    private ShippingService shippingService;
//
//    @ManyToOne
//    @JoinColumn(name = "level_id")
//    private PricingLevel pricingLevel;
//
//    @Embedded
//    private AuditSection auditSection = new AuditSection();
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Column(name = "total_cost_for_weight_range")
//    private TotalCostForWeightRange totalCostForWeightRange;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricing_id", nullable = false)
    private Integer pricingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ShippingService shippingService;

    @Column(name = "weight_from", nullable = true, precision = 10, scale = 2)
    private BigDecimal weightFrom;

    @Column(name = "weight_to", nullable = true, precision = 10, scale = 2)
    private BigDecimal weightTo;

    @Column(name = "base_unit", nullable = true, length = 2)
    private String baseUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "currency_code", nullable = true, length = 3)
    private String currencyCode;

    @Column(name = "rate", nullable = true, precision = 10, scale = 2)
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private PricingLevel pricingLevel;

    @Column(name = "pricing_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PricingType pricingType;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "create_ts", nullable = true)
    private Integer createTs;

    @Column(name = "update_ts", nullable = true)
    private Integer updateTs;

}
