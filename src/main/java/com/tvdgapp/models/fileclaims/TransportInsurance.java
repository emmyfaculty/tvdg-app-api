package com.tvdgapp.models.fileclaims;

import lombok.Getter;

public enum TransportInsurance {

    TOLADOL_CARGO_INSURANCE("Toladol cargo insurance"),
    ANOTHER_INSURANCE_COMPANY("Another Insurance Company"),
    NONE("None");

    @Getter
    private String display;


    TransportInsurance(String display) {
        this.display = display;
    }
}
