package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.pricingcaculation.PricingLevelRequestDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.PricingLevelResponseDto;
import com.tvdgapp.services.shipment.pricingcaculation.PricingLevelService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pricing-levels")
public class PricingLevelController {

    @Autowired
    private PricingLevelService pricingLevelService;

    @PostMapping
    @PreAuthorize("hasAuthority('managePricingLevel')")
    public ResponseEntity<ApiDataResponse<PricingLevelResponseDto>> createPricingLevel(@RequestBody PricingLevelRequestDto requestDto) {
        PricingLevelResponseDto responseDto = pricingLevelService.createPricingLevel(requestDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, responseDto, "Resource created successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('managePricingLevel')")
    public ResponseEntity<ApiDataResponse<List<PricingLevelResponseDto>>> getAllPricingLevels() {
        List<PricingLevelResponseDto> responseDtos = pricingLevelService.getAllPricingLevels();
        return ApiResponseUtils.response(HttpStatus.OK, responseDtos, "Resources retrieved successfully");
    }

    @GetMapping("/{levelId}")
    public ResponseEntity<ApiDataResponse<PricingLevelResponseDto>> getPricingLevelById(@PathVariable Integer levelId) {
        PricingLevelResponseDto responseDto = pricingLevelService.getPricingLevelById(levelId);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");
    }

    @PutMapping("/{levelId}")
    @PreAuthorize("hasAuthority('managePricingLevel')")
    public ResponseEntity<ApiDataResponse<PricingLevelResponseDto>> updatePricingLevel(@PathVariable Integer levelId, @RequestBody PricingLevelRequestDto requestDto) {
        PricingLevelResponseDto responseDto = pricingLevelService.updatePricingLevel(levelId, requestDto);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources updated successfully");
    }

    @DeleteMapping("/{levelId}")
    @PreAuthorize("hasAuthority('managePricingLevel')")
    public ResponseEntity<ApiDataResponse<Object>> deletePricingLevel(@PathVariable Integer levelId) {
        pricingLevelService.deletePricingLevel(levelId);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");

    }
}
