package com.tvdgapp.models.shipment;

import lombok.Getter;

public enum ServiceInterest {
    ECOMMERCE("ecommerce"),
    PROCUREMENT("procurement"),
    GLOBAL_SHIPMENT("global shipment"),
    NATIONWIDE_SHIPMENT("nationwide shipment"),
    RIDERS_TRAINING("rider training"),
    OUTSOURCING("outsourcing");

    @Getter
    private String display;


    ServiceInterest(String display) {
        this.display = display;
    }
}
