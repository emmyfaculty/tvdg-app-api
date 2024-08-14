package com.tvdgapp.controllers.city;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.models.reference.countrycode.LocaleCity;
import com.tvdgapp.models.reference.countrycode.LocaleCityDto;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import com.tvdgapp.services.reference.city.CityService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;
//    @Override
    @GetMapping
    public ResponseEntity<ApiDataResponse<Collection<LocaleCityDto>>> listCities() throws Exception {
        Collection<LocaleCityDto> list = this.cityService.listLocaleCities();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiDataResponse<LocaleCity>> getCityByName(@PathVariable String name) throws Exception {
        LocaleCity city = this.cityService.getCityByName(name);
        return ApiResponseUtils.response(HttpStatus.OK, city, "Resources retrieved successfully");
    }
    @GetMapping("/{stateCode}")
    public ResponseEntity<ApiDataResponse<List<LocaleCity>>> getStateByStateCode(@PathVariable String stateCode) throws Exception {
        List<LocaleCity> city = this.cityService.getCitiesByStateCode(stateCode);
        return ApiResponseUtils.response(HttpStatus.OK, city, "Resources retrieved successfully");
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiDataResponse<Optional<LocaleCity>>> getCityById(@PathVariable Integer id) throws Exception {
        Optional<LocaleCity> city = this.cityService.getCityById(id);
        return ApiResponseUtils.response(HttpStatus.OK, city, "Resources retrieved successfully");
    }

}
