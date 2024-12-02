package com.tvdgapp.dtos;

import com.tvdgapp.models.reference.Region;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class InternationalRegionsCountriesResponseDto {

    private Long countryId;
    private String countryName;
    private String iso2;
    private String iso3;
    private Region region;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Getters and Setters
}
