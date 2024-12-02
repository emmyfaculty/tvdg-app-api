//package com.tvdgapp.mapper;
//
//import com.tvdgapp.dtos.CountryDTO;
//import com.tvdgapp.models.reference.Country;
//import com.tvdgapp.models.reference.Region;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CountryMapper1 {
//
//    public CountryDTO toDto(Country country) {
//        if (country == null) {
//            return null;
//        }
//
//        CountryDTO dto = new CountryDTO();
//        dto.setId(country.getId());
//        dto.setName(country.getName());
//        dto.setIso3(country.getIso3());
//        dto.setIso2(country.getIso2());
//        dto.setRegionId(country.getRegion() != null ? country.getRegion().getRegionId() : null);
//
//        return dto;
//    }
//
//    public Country toEntity(CountryDTO dto, Region region) {
//        if (dto == null) {
//            return null;
//        }
//
//        Country country = new Country();
//        country.setId(dto.getId());
//        country.setName(dto.getName());
//        country.setIso3(dto.getIso3());
//        country.setIso2(dto.getIso2());
//        country.setRegion(region);
//
//        return country;
//    }
//}
