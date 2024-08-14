package com.tvdgapp.services.reference;

import com.tvdgapp.dtos.RegionDTO;

import com.tvdgapp.mapper.CountryMapper;
import com.tvdgapp.mapper.RegionMapper;
import com.tvdgapp.models.reference.Country;
import com.tvdgapp.models.reference.Region;

import com.tvdgapp.repositories.reference.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private CountryMapper countryMapper;

    @Transactional
    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(regionMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public RegionDTO getRegionById(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region not found with id: " + id));
        return regionMapper.toDto(region);
    }

    @Transactional
    public RegionDTO createRegion(RegionDTO regionDTO) {
        if (regionRepository.findByRegionName(regionDTO.getRegionName()).isPresent()) {
            throw new IllegalArgumentException("Region with name " + regionDTO.getRegionDescription() + " already exists");
        }
        Region region = regionMapper.toEntity(regionDTO);

        // Ensure countries are not required
        List<Country> countries = null;
        if (regionDTO.getCountries() != null) {
            countries = regionDTO.getCountries().stream()
                    .map(countryDTO -> countryMapper.toEntity(countryDTO))
                    .collect(Collectors.toList());
        }
        region.setCountries(countries);

        Region savedRegion = regionRepository.save(region);
        return regionMapper.toDto(savedRegion);
    }

    @Transactional
    public RegionDTO updateRegion(Integer id, RegionDTO regionDTO) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region not found with id: " + id));

        region.setRegionName(regionDTO.getRegionName());

        // Update countries if provided
        List<Country> countries = null;
        if (regionDTO.getCountries() != null) {
            countries = regionDTO.getCountries().stream()
                    .map(countryDTO -> countryMapper.toEntity(countryDTO))
                    .collect(Collectors.toList());
        }
        region.setCountries(countries);

        Region updatedRegion = regionRepository.save(region);
        return regionMapper.toDto(updatedRegion);
    }

    @Transactional
    public void deleteRegion(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region not found with id: " + id));
        regionRepository.delete(region);
    }

    public Region findRegionById(Integer regionId) {
        Region region = regionRepository.findById(regionId).orElseThrow(() -> new IllegalArgumentException("Region not found with id: " + regionId));
        return region;
    }
}