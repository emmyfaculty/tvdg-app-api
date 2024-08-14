package com.tvdgapp.dtos.affiliate;

import lombok.Data;

@Data
public class WithdrawalRequestDto {
    public String bankName;
    private double amount;
    public Long accountNumber;
}

