package com.tvdgapp.models.fileclaims;

import lombok.Getter;

public enum ClaimType {

    LOST_SHIPMENT("Lost shipment"),
    DAMAGE_SHIPMENT("Damage shipment"),
    LOST_ITEM("Lost item"),
    DAMAGE_ITEM("Damage item");

    @Getter
    private String display;


    ClaimType(String display) {
        this.display = display;
    }
}
