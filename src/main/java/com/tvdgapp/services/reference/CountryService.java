package com.tvdgapp.services.reference;

import com.tvdgapp.dtos.CountryDTO;
import com.tvdgapp.dtos.InternationalRegionsCountriesResponseDto;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.reference.Country;
import com.tvdgapp.models.reference.Region;
import com.tvdgapp.repositories.reference.CountryRepository;
import com.tvdgapp.repositories.reference.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;


    public List<InternationalRegionsCountriesResponseDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public InternationalRegionsCountriesResponseDto getCountryById(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        return toResponseDto(country);
    }

    private CountryDTO convertToDTO(Country country) {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryName(country.getCountryName());
        countryDTO.setIso3(country.getIso3());
        countryDTO.setIso2(country.getIso2());
        countryDTO.setRegionId(country.getRegion().getRegionId());
        return countryDTO;
    }

    public InternationalRegionsCountriesResponseDto createCountry(CountryDTO countryDTO) {
        Region region = regionRepository.findById(countryDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));

        if (countryRepository.existsByCountryNameAndRegion(countryDTO.getCountryName(), region)) {
            throw new IllegalArgumentException("Country with name " + countryDTO.getCountryName() + " already exists in region " + region.getRegionName());
        }

        Country country = new Country();
        country.setCountryName(countryDTO.getCountryName());
        country.setIso3(countryDTO.getIso3());
        country.setIso2(countryDTO.getIso2());
        country.setRegion(region);

        country = countryRepository.save(country);

        return toResponseDto(country);
    }


    @Transactional
    public InternationalRegionsCountriesResponseDto updateCountry(Integer id, CountryDTO countryDTO) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));

        Region region = regionRepository.findById(countryDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Region not found with id: " + countryDTO.getRegionId()));

        country.setCountryName(countryDTO.getCountryName());
        country.setIso2(countryDTO.getIso2());
        country.setIso3(countryDTO.getIso3());
        country.setRegion(region);

        country = countryRepository.save(country);
        return toResponseDto(country);
    }

    @Transactional
    public void deleteCountry(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        countryRepository.delete(country);
    }

    public Country toEntity(CountryDTO dto) {
        Country entity = new Country();
        entity.setCountryName(dto.getCountryName());
        entity.setIso2(dto.getIso2());
        entity.setIso3(dto.getIso3());

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id " + dto.getRegionId()));
        entity.setRegion(region);

        return entity;
    }

    public InternationalRegionsCountriesResponseDto toResponseDto(Country entity) {
        InternationalRegionsCountriesResponseDto dto = new InternationalRegionsCountriesResponseDto();
        dto.setCountryId(Long.valueOf(entity.getCountryId()));
        dto.setCountryName(entity.getCountryName());
        dto.setIso2(entity.getIso2());
        dto.setIso3(entity.getIso3());
        dto.setRegion(entity.getRegion());
        dto.setCreatedAt(LocalDate.from(entity.getCreatedAt()));
        dto.setUpdatedAt(LocalDate.from(entity.getUpdatedAt()));

        return dto;
    }
}
