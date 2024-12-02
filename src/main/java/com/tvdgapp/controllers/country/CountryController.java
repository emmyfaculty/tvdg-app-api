package com.tvdgapp.controllers.country;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleCountryDto;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import com.tvdgapp.services.reference.country.CountryService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;
//    @Override
    @GetMapping
    public ResponseEntity<ApiDataResponse<Collection<LocaleCountryDto>>> listCountries() throws Exception {
        Collection<LocaleCountryDto> list = this.countryService.listCountries();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiDataResponse<LocaleCountry>> getCountryByName(@PathVariable String name) throws Exception {
        LocaleCountry country = this.countryService.getCountryByName(name);
        return ApiResponseUtils.response(HttpStatus.OK, country, "Resources retrieved successfully");
    }
    @GetMapping("/iso2/{iso2}")
    public ResponseEntity<ApiDataResponse<LocaleCountry>> getStateByIso2(@PathVariable String iso2) throws Exception {
        LocaleCountry country = this.countryService.getCountryByISO2(iso2);
        return ApiResponseUtils.response(HttpStatus.OK, country, "Resources retrieved successfully");
    }
    @GetMapping("/iso3/{iso3}")
    public ResponseEntity<ApiDataResponse<LocaleCountry>> getStateByIso3(@PathVariable String iso3) throws Exception {
        LocaleCountry country = this.countryService.getCountryByISO3(iso3);
        return ApiResponseUtils.response(HttpStatus.OK, country, "Resources retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Optional<LocaleCountry>>> getStateById(@PathVariable Integer id) throws Exception {
        Optional<LocaleCountry> country = this.countryService.getLocaleCountryById(id);
        return ApiResponseUtils.response(HttpStatus.OK, country, "Resources retrieved successfully");
    }

}
