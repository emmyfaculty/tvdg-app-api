package com.tvdgapp.models.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.Units;
import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.models.reference.Region;
import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import com.tvdgapp.models.shipment.nationwide.TotalCostForWeightRange;
import com.tvdgapp.models.wallet.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.tvdgapp.constants.SchemaConstant.TABLE_INTERNATIONAL_SHIPPING_PRICE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name= TABLE_INTERNATIONAL_SHIPPING_PRICE)
//@ValidWeightBand
public class PriceModelLevel extends TvdgAppEntity<Long, PriceModelLevel> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.0", message = "Weight band start must be greater than or equal to 0")
    private BigDecimal weightBandStart;

    @DecimalMin(value = "0.0", inclusive = false, message = "Weight band end must be greater than 0")
    private BigDecimal weightBandEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Units units;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nation_wide_region_id")
    private NationWideRegion nationWideRegion;

    @NotNull
    private String description;

    @NotNull
    private double price;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ShippingService shippingService;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private PricingLevel pricingLevel;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "total_cost_for_weight_range")
    private TotalCostForWeightRange totalCostForWeightRange;

}
