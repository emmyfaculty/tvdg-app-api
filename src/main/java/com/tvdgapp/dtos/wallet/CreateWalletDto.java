package com.tvdgapp.dtos.wallet;

import lombok.Data;

@Data
public class CreateWalletDto {
    private Long userId;
    private String currency;
}
