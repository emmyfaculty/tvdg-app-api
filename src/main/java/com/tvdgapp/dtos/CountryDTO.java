package com.tvdgapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {
    @NotNull(message = "Country name is required")
    @Size(min = 1, max = 100, message = "Country name must be between 1 and 100 characters")
    private String countryName;

    @NotNull(message = "ISO2 code is required")
    @Size(min = 2, max = 2, message = "ISO2 code must be 2 characters")
    private String iso2;

    @NotNull(message = "ISO3 code is required")
    @Size(min = 3, max = 3, message = "ISO3 code must be 3 characters")
    private String iso3;

    @NotNull(message = "Region ID is required")
    private Integer regionId;
}
