package com.tvdgapp.dtos.wallet1;

import java.math.BigDecimal;

public class PaymentRequest {
    private Long walletId;
    private BigDecimal amount;

    // Getters and Setters
    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}