package com.tvdgapp.dtos.user;

import com.tvdgapp.dtos.auth.UserDto;
import com.tvdgapp.dtos.wallet.WalletDto;
import lombok.Data;

@Data
public class CustomerUserDtoResponse {

    private UserDto user;
    private CustomerDtoResponse customer;
    private WalletDto wallet;
}
