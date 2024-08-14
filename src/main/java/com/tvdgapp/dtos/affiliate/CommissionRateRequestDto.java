package com.tvdgapp.dtos.affiliate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionRateRequestDto {

    @NotNull
    @Min(0)
    private BigDecimal minSalesAmount;

    @Min(0)
    private BigDecimal maxSalesAmount;

    @NotNull
    @Min(0)
    @Max(100)
    private BigDecimal commissionPercentage;

}
