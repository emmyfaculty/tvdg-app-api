package com.tvdgapp.mapper;

import com.tvdgapp.dtos.CountryDTO;
import com.tvdgapp.models.reference.Country;
import com.tvdgapp.models.reference.Region;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO toDto(Country country);

    @Mapping(target = "region", ignore = true)
    Country toEntity(CountryDTO countryDTO);
}
