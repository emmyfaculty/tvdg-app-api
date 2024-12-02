package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.pricingcaculation.CreatePriceModelLevelDto;
import com.tvdgapp.services.shipment.pricingcaculation.PriceModelLevelService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipping-prices")
public class PriceModelLevelController {
    private final PriceModelLevelService priceModelLevelService;

    @PostMapping
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<CreatePriceModelLevelDto>> createPriceModelLevel(@RequestBody CreatePriceModelLevelDto priceModelLevelDTO) {
        CreatePriceModelLevelDto createdLevel = priceModelLevelService.createPriceModelLevel(priceModelLevelDTO);
        return ApiResponseUtils.response(HttpStatus.OK, createdLevel, "Resources retrieved successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<List<CreatePriceModelLevelDto>>> getAllPriceModelLevels() {
        List<CreatePriceModelLevelDto> list = priceModelLevelService.getAllPriceModelLevels();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<CreatePriceModelLevelDto>> getPriceModelLevelById(@PathVariable Long id) {
        CreatePriceModelLevelDto level = priceModelLevelService.getPriceModelLevelById(id);
        return ApiResponseUtils.response(HttpStatus.OK, level, "Resources retrieved successfully");
    }

    @GetMapping("/by-service-portfolio/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<List<CreatePriceModelLevelDto>>> getPriceModelLevelsByServicePortfolioId(@PathVariable Long id) {
        List<CreatePriceModelLevelDto> list = priceModelLevelService.getPriceModelLevelsByServicePortfolioId(id);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<CreatePriceModelLevelDto>> updatePriceModelLevel(@PathVariable Long id, @RequestBody CreatePriceModelLevelDto priceModelLevelDTO) {
        CreatePriceModelLevelDto updatedLevel = priceModelLevelService.updatePriceModelLevel(id, priceModelLevelDTO);
        return ApiResponseUtils.response(HttpStatus.OK, updatedLevel, "Resources updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<Object>> deletePriceModelLevel(@PathVariable Long id) {
        priceModelLevelService.deletePriceModelLevel(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");

    }
}
