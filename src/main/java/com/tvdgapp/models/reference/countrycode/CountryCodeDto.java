package com.tvdgapp.models.reference.countrycode;

import jakarta.annotation.Nullable;
import lombok.Data;


@Data
public class CountryCodeDto {

    private int id;
    @Nullable
    private String countryName;
    @Nullable
    private String code;

    public CountryCodeDto(@Nullable String countryName, @Nullable String dialCode) {
        this.countryName = countryName;
        this.code = dialCode;
    }

}
