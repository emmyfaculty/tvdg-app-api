package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.pricingcaculation.PickupLocationDTO;
import com.tvdgapp.services.shipment.pricingcaculation.PickupLocationService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pickup-locations")
public class PickupLocationController {

    @Autowired
    private PickupLocationService pickupLocationService;

    @GetMapping
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<List<PickupLocationDTO>>> getAllPickupLocations() {
        List<PickupLocationDTO> responseDto =  pickupLocationService.findAll();
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<PickupLocationDTO>> getPickupLocationById(@PathVariable Long id) {
        PickupLocationDTO responseDto = pickupLocationService.findById(id);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");
    }

    @PostMapping
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<PickupLocationDTO>> createPickupLocation(@RequestBody PickupLocationDTO pickupLocationDTO) {
        PickupLocationDTO responseDto = pickupLocationService.save(pickupLocationDTO);
        return ApiResponseUtils.response(HttpStatus.CREATED, responseDto, "Resource created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<PickupLocationDTO>> updatePickupLocation(@PathVariable Long id, @RequestBody PickupLocationDTO pickupLocationDTO) {
        PickupLocationDTO responseDto = pickupLocationService.update(id, pickupLocationDTO);
        return ApiResponseUtils.response(HttpStatus.CREATED, responseDto, "Resource created successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('managePickupLocation')")
    public ResponseEntity<ApiDataResponse<Object>> deletePickupLocation(@PathVariable Long id) {
        pickupLocationService.delete(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }
}
