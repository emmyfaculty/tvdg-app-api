package com.tvdgapp.dtos.wallet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceResponseDto {
    private Long userId;
    private BigDecimal balance;

    public BalanceResponseDto(Long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

}
