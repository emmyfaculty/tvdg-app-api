package com.tvdgapp.dtos.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.Units;
import com.tvdgapp.models.shipment.nationwide.TotalCostForWeightRange;
import com.tvdgapp.models.wallet.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePriceModelLevelDto {
//    @NotNull(message = "Weight band start is required")
//    @DecimalMin(value = "0.0", message = "Weight band start must be greater than or equal to 0")
//    private BigDecimal weightBandStart;
//
//    @DecimalMin(value = "0.0", inclusive = false, message = "Weight band end must be greater than 0")
//    private BigDecimal weightBandEnd;
    private Integer pricingId;
    private Integer serviceId;
    @NotNull(message = "Weight band start is required")
    @DecimalMin(value = "0.0", message = "Weight band start must be greater than or equal to 0")
    private BigDecimal weightFrom;
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight band end must be greater than 0")
    private BigDecimal weightTo;
    private String baseUnit;
    private Integer regionId;
    private String currencyCode;
    private BigDecimal rate;
    private Integer levelId;
    private String pricingType;
    private String description;
}