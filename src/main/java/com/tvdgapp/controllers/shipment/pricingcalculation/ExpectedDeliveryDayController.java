package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.services.shipment.pricingcaculation.ExpectedDeliveryDayService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tvdgapp.dtos.shipment.pricingcaculation.ExpectedDeliveryDayDTO;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expected-delivery-days")
public class ExpectedDeliveryDayController {

    private final ExpectedDeliveryDayService expectedDeliveryDayService;

    @PostMapping
    @PreAuthorize("hasAuthority('manageExpectedDeliveryDays')")
    public ResponseEntity<ApiDataResponse<ExpectedDeliveryDayDTO>> createExpectedDeliveryDay(@RequestBody ExpectedDeliveryDayDTO expectedDeliveryDayDTO) {
        ExpectedDeliveryDayDTO createdExpectedDeliveryDay = expectedDeliveryDayService.createExpectedDeliveryDay(expectedDeliveryDayDTO);
        return ApiResponseUtils.response(HttpStatus.CREATED, createdExpectedDeliveryDay, "Resource created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('manageExpectedDeliveryDays')")
    public ResponseEntity<ApiDataResponse<ExpectedDeliveryDayDTO>> updateExpectedDeliveryDay(
            @PathVariable Long id,
            @RequestBody ExpectedDeliveryDayDTO expectedDeliveryDayDTO) {
        ExpectedDeliveryDayDTO updatedExpectedDeliveryDay = expectedDeliveryDayService.updateExpectedDeliveryDay(id, expectedDeliveryDayDTO);
        return ApiResponseUtils.response(HttpStatus.OK, updatedExpectedDeliveryDay, "Resources updated successfully");

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('manageExpectedDeliveryDays')")
    public ResponseEntity<ApiDataResponse<Object>> deleteExpectedDeliveryDay(@PathVariable Long id) {
        expectedDeliveryDayService.deleteExpectedDeliveryDay(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('manageExpectedDeliveryDays')")
    public ResponseEntity<ApiDataResponse<ExpectedDeliveryDayDTO>> getExpectedDeliveryDay(@PathVariable Long id) {
        ExpectedDeliveryDayDTO expectedDeliveryDayDTO = expectedDeliveryDayService.getExpectedDeliveryDayById(id);
        return ApiResponseUtils.response(HttpStatus.OK, expectedDeliveryDayDTO, "Resources retrieved successfully");    }

    @GetMapping
    @PreAuthorize("hasAuthority('manageExpectedDeliveryDays')")
    public ResponseEntity<ApiDataResponse<List<ExpectedDeliveryDayDTO>>> getAllExpectedDeliveryDays() {
        List<ExpectedDeliveryDayDTO> expectedDeliveryDays = expectedDeliveryDayService.getAllExpectedDeliveryDays();
        return ApiResponseUtils.response(HttpStatus.OK, expectedDeliveryDays, "Resources retrieved successfully");
    }
}
