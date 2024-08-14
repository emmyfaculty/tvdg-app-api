package com.tvdgapp.dtos.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.Units;
import com.tvdgapp.models.shipment.nationwide.TotalCostForWeightRange;
import com.tvdgapp.models.wallet.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePriceModelLevelDto {
    @NotNull(message = "Weight band start is required")
    @DecimalMin(value = "0.0", message = "Weight band start must be greater than or equal to 0")
    private BigDecimal weightBandStart;

    @DecimalMin(value = "0.0", inclusive = false, message = "Weight band end must be greater than 0")
    private BigDecimal weightBandEnd;

    @NotNull(message = "Units is required")
    private Units units;

//    @NotNull(message = "Region ID is required")
    private Integer regionId;

    private Integer nationWideRegionId;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    private double price;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "Service ID is required")
    private Integer serviceId;

    @NotNull(message = "Pricing level ID is required")
    private Integer pricingLevelId;

    @NotNull(message = "Total cost for weight range is required")
    private TotalCostForWeightRange totalCostForWeightRange;
}