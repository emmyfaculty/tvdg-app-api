package com.tvdgapp.dtos.wallet;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;


@Data
//@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class WalletDto {

    private Long walletId;

    private Long userId;

    private BigDecimal balance;

    private String walletKey;

    public WalletDto(Long walletId, Long userId, BigDecimal balance, String walletKey){

        this.walletId = walletId;
        this.userId = userId;
        this.balance = balance;
        this.walletKey = walletKey;
    }

}
