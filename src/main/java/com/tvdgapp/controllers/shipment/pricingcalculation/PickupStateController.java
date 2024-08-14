package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.pricingcaculation.PickupStateDTO;
import com.tvdgapp.services.shipment.pricingcaculation.PickupStateService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pickup-states")
public class PickupStateController {

    @Autowired
    private PickupStateService pickupStateService;

    @PostMapping
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<PickupStateDTO>> createPickupState(@RequestBody PickupStateDTO pickupStateDTO) {
        PickupStateDTO createdPickupState = pickupStateService.createPickupState(pickupStateDTO);
        return ApiResponseUtils.response(HttpStatus.CREATED, createdPickupState, "Resource created successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<PickupStateDTO>> getPickupStateById(@PathVariable Long id) {
        PickupStateDTO pickupStateDTO = pickupStateService.getPickupStateById(id);
        return ApiResponseUtils.response(HttpStatus.OK, pickupStateDTO, "Resources retrieved successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<List<PickupStateDTO>>> getAllPickupStates() {
        List<PickupStateDTO> pickupStates = pickupStateService.getAllPickupStates();
        return ApiResponseUtils.response(HttpStatus.OK, pickupStates, "Resources retrieved successfully");

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<PickupStateDTO>> updatePickupState(@PathVariable Long id, @RequestBody PickupStateDTO pickupStateDTO) {
        PickupStateDTO updatedPickupState = pickupStateService.updatePickupState(id, pickupStateDTO);
        return ApiResponseUtils.response(HttpStatus.CREATED, updatedPickupState, "Resource updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<Object>> deletePickupState(@PathVariable Long id) {
        pickupStateService.deletePickupState(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }
}
