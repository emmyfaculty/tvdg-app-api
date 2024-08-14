package com.tvdgapp.dtos.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.Units;
import com.tvdgapp.models.shipment.nationwide.TotalCostForWeightRange;
import com.tvdgapp.models.wallet.Currency;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
//@ValidWeightBand
public class PriceModelLevelDTO {
//    private Long id;
//    @NotNull
//    @DecimalMin(value = "0.0", inclusive = true, message = "Weight band start must be greater than or equal to 0")
//    private BigDecimal weightBandStart;
//
//    @DecimalMin(value = "0.0", inclusive = false, message = "Weight band end must be greater than 0")
//    private BigDecimal weightBandEnd;
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private String units;
//
//    private Long regionId;  // ID of the associated region
//
//    @NotNull
//    private double price;
//    private String description;
//    private Currency currency;
//
//    private Integer pricingLevelId;
//
//    private TotalCostForWeightRange totalCostForWeightRange;
//    @Nullable
//    private Integer serviceId;
    private Long id;
    private BigDecimal weightBandStart;
    private BigDecimal weightBandEnd;
    private Units units;
    private Long regionId;
    private Long nationWideRegionId;
    private String description;
    private double price;
    private Currency currency;
    private Long serviceId;
    private Integer pricingLevelId;
    private TotalCostForWeightRange totalCostForWeightRange;

}
