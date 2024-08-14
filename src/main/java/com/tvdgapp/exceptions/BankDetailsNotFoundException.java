package com.tvdgapp.exceptions;

public class BankDetailsNotFoundException extends RuntimeException {
    public BankDetailsNotFoundException(String message) {
        super(message);
    }
}
