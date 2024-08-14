package com.tvdgapp.controllers;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.CountryDTO;
import com.tvdgapp.services.reference.CountryService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/price-countries")
public class PriceCountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<CountryDTO>>> getAllCountries() {
        List<CountryDTO> list = countryService.getAllCountries();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<CountryDTO>> getCountryById(@PathVariable Integer id) {
        CountryDTO country = countryService.getCountryById(id);
        return ApiResponseUtils.response(HttpStatus.OK, country, "Resources retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<CountryDTO>> createCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO createdCountry = countryService.createCountry(countryDTO);
        return ApiResponseUtils.response(HttpStatus.OK, createdCountry, "Resources retrieved successfully");

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<CountryDTO>> updateCountry(@PathVariable Integer id, @RequestBody CountryDTO countryDTO) {
        CountryDTO updatedCountry = countryService.updateCountry(id, countryDTO);
        return ApiResponseUtils.response(HttpStatus.OK, updatedCountry, "Resources retrieved successfully");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>> deleteCountry(@PathVariable Integer id) {
        countryService.deleteCountry(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");

    }
}