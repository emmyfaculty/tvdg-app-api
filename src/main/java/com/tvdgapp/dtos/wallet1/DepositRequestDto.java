package com.tvdgapp.dtos.wallet1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequestDto {
    private Long customerId;
    private BigDecimal amount;

}
