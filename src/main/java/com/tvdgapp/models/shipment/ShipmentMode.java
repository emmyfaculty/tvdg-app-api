package com.tvdgapp.models.shipment;

import lombok.Getter;

public enum ShipmentMode {
    AIR("air"),
    SEA("sea"),
    ROAD("road");

    @Getter
    private String display;


    ShipmentMode(String display) {
        this.display = display;
    }
}
