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
    private Integer id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @Size(max = 3)
    private String iso3;

    @Size(max = 2)
    private String iso2;

    private Integer regionId;
}
