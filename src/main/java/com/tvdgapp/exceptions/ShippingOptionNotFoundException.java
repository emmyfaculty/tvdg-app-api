package com.tvdgapp.exceptions;

public class ShippingOptionNotFoundException extends RuntimeException {
    public ShippingOptionNotFoundException(String message) {
        super(message);
    }
}
