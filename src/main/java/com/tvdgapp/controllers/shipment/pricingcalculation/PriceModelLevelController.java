package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.pricingcaculation.CreatePriceModelLevelDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.PriceModelLevelDTO;
import com.tvdgapp.dtos.shipment.pricingcaculation.UpdatePriceModelLevelDto;
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
    public ResponseEntity<ApiDataResponse<PriceModelLevelDTO>> createPriceModelLevel(@RequestBody CreatePriceModelLevelDto priceModelLevelDTO) {
        PriceModelLevelDTO createdLevel = priceModelLevelService.createPriceModelLevel(priceModelLevelDTO);
        return ApiResponseUtils.response(HttpStatus.OK, createdLevel, "Resources retrieved successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<List<PriceModelLevelDTO>>> getAllPriceModelLevels() {
        List<PriceModelLevelDTO> list = priceModelLevelService.getAllPriceModelLevels();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<PriceModelLevelDTO>> getPriceModelLevelById(@PathVariable Long id) {
        PriceModelLevelDTO level = priceModelLevelService.getPriceModelLevelById(id);
        return ApiResponseUtils.response(HttpStatus.OK, level, "Resources retrieved successfully");
    }

    @GetMapping("/by-service-portfolio/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<List<PriceModelLevelDTO>>> getPriceModelLevelsByServicePortfolioId(@PathVariable Long id) {
        List<PriceModelLevelDTO> list = priceModelLevelService.getPriceModelLevelsByServicePortfolioId(id);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<PriceModelLevelDTO>> updatePriceModelLevel(@PathVariable Long id, @RequestBody UpdatePriceModelLevelDto priceModelLevelDTO) {
        PriceModelLevelDTO updatedLevel = priceModelLevelService.updatePriceModelLevel(id, priceModelLevelDTO);
        return ApiResponseUtils.response(HttpStatus.OK, updatedLevel, "Resources updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('manageShippingPrice')")
    public ResponseEntity<ApiDataResponse<Object>> deletePriceModelLevel(@PathVariable Long id) {
        priceModelLevelService.deletePriceModelLevel(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");

    }
}
