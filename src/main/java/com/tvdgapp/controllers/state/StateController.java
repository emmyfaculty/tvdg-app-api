package com.tvdgapp.controllers.state;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.models.reference.countrycode.LocaleCity;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import com.tvdgapp.services.reference.states.StatesService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/states")
public class StateController {

    private final StatesService statesService;
//    @Override
    @GetMapping
    public ResponseEntity<ApiDataResponse<Collection<LocaleStateDto>>> listStates() throws Exception {
        Collection<LocaleStateDto> list = this.statesService.listStates();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiDataResponse<LocaleState>> getStateByName(@PathVariable String name) throws Exception {
        LocaleState state = this.statesService.getStateByName(name);
        return ApiResponseUtils.response(HttpStatus.OK, state, "Resources retrieved successfully");
    }
    @GetMapping("/iso2")
    public ResponseEntity<ApiDataResponse<LocaleState>> getStateByIso2(@RequestParam String iso2, @RequestParam String countryCode) throws Exception {
        LocaleState state = this.statesService.getStateByIso2(iso2, countryCode);
        return ApiResponseUtils.response(HttpStatus.OK, state, "Resources retrieved successfully");
    }

    @GetMapping("/{countryCode}")
    public ResponseEntity<ApiDataResponse<List<LocaleState>>> getStateByCountryCode(@PathVariable String countryCode) throws Exception {
        List<LocaleState> state = this.statesService.getStatesByCountryCode(countryCode);
        return ApiResponseUtils.response(HttpStatus.OK, state, "Resources retrieved successfully");
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiDataResponse<Optional<LocaleState>>> getStateById(@PathVariable Integer id) throws Exception {
        Optional<LocaleState> state = this.statesService.getStateById(id);
        return ApiResponseUtils.response(HttpStatus.OK, state, "Resources retrieved successfully");
    }

}
