package com.tvdgapp.services.reference;

import com.tvdgapp.dtos.CountryDTO;

import com.tvdgapp.mapper.CountryMapper1;
import com.tvdgapp.models.reference.Country;
import com.tvdgapp.models.reference.Region;

import com.tvdgapp.repositories.reference.CountryRepository;
import com.tvdgapp.repositories.reference.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CountryMapper1 countryMapper1;

    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(countryMapper1::toDto)
                .collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        return countryMapper1.toDto(country);
    }

    private CountryDTO convertToDTO(Country country) {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(country.getId());
        countryDTO.setName(country.getName());
        countryDTO.setIso3(country.getIso3());
        countryDTO.setIso2(country.getIso2());
        countryDTO.setRegionId(country.getRegion().getRegionId());
        return countryDTO;
    }

    public CountryDTO createCountry(CountryDTO countryDTO) {
        Region region = regionRepository.findById(countryDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));

        if (countryRepository.existsByNameAndRegion(countryDTO.getName(), region)) {
            throw new IllegalArgumentException("Country with name " + countryDTO.getName() + " already exists in region " + region.getRegionName());
        }

        Country country = new Country();
        country.setName(countryDTO.getName());
        country.setIso3(countryDTO.getIso3());
        country.setIso2(countryDTO.getIso2());
        country.setRegion(region);

        country = countryRepository.save(country);

        return convertToDTO(country);
    }


    @Transactional
    public CountryDTO updateCountry(Integer id, CountryDTO countryDTO) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));

        Region region = regionRepository.findById(countryDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Region not found with id: " + countryDTO.getRegionId()));

        country.setName(countryDTO.getName());
        country.setIso2(countryDTO.getIso2());
        country.setIso3(countryDTO.getIso3());
        country.setRegion(region);

        country = countryRepository.save(country);
        return countryMapper1.toDto(country);
    }

    @Transactional
    public void deleteCountry(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        countryRepository.delete(country);
    }
}
