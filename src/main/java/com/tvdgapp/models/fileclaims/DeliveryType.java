package com.tvdgapp.models.fileclaims;

import lombok.Getter;

public enum DeliveryType {
    DOOR_STEP_DELIVERY("door step delivery"),
    ACCESS_POINT("access point"),
    OTHERS("others");

    @Getter
    private String display;


    DeliveryType(String display) {
        this.display = display;
    }
}
