package com.tvdgapp.models.shipment;

import lombok.Getter;

public enum PaymentMethod {

    DEBIT_CARD("Debit Card"),
    BANK_TRANSFER("Bank Transfer"),
    BANK_DEPOSIT("Bank Deposit"),
    PAY_STACK("Paystack"),
    WALLET("wallet");

    @Getter
    private String display;


    PaymentMethod(String display) {
        this.display = display;
    }
}
