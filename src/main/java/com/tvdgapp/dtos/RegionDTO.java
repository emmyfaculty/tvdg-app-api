package com.tvdgapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {
    private Integer id;

//    @NotNull
//    @Size(min = 2, max = 50)
//    private String name;
    @NotNull
    private String regionName;
    private String regionDescription;

    private List<CountryDTO> countries;
}
