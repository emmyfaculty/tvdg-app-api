package com.tvdgapp.dtos.auth;

import com.tvdgapp.dtos.user.CustomerDtoResponse;
import com.tvdgapp.dtos.wallet.WalletDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto{

    private UserDto user;
    private CustomerDtoResponse customer;
    private WalletDto wallet;
    private String message;
    private String token;
    private String refreshToken;
    private Collection<String> authorities;

    public LoginResponseDto(UserDto userDto, CustomerDtoResponse customerUserDto,  WalletDto walletDto,  String token) {
        this.user = userDto;
        this.customer = customerUserDto;
        this.token = token;
        this.wallet = walletDto;
    }


    public LoginResponseDto(UserDto userDto, String token) {
        this.user = userDto;
        this.token=token;
    }    public LoginResponseDto(UserDto userDto) {
        this.user = userDto;
    }

    public LoginResponseDto(String message) {
        this.message = message;
    }

}
