package com.tvdgapp.controllers.shipment;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.shipment.ShippingProfileDto;
import com.tvdgapp.dtos.shipment.ShippingProfileRequestDto;
import com.tvdgapp.models.shipment.ShippingProfile;
import com.tvdgapp.services.shipment.ShippingProfileService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipment-profiles")
public class ShipmentProfileController {


    private final ShippingProfileService shippingProfileService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('manageShippingProfile')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> createShipmentProfile(@RequestBody ShippingProfileRequestDto profileDto) {
        ShippingProfile shippingProfile = shippingProfileService.createShippingProfile(profileDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(shippingProfile.getId()), "Resource created successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('manageShippingProfile')")
    public ResponseEntity<ApiDataResponse<List<ShippingProfileDto>>> listShipmentProfiles() {
        List<ShippingProfileDto> shippingProfiles = shippingProfileService.getAllShippingProfiles();
        return ApiResponseUtils.response(HttpStatus.CREATED, shippingProfiles, "Resource created successfully");
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('manageShippingProfile')")
    public ResponseEntity<ApiDataResponse<List<ShippingProfile>>> getShippingProfilesByCustomerId(@PathVariable Long customerId) {
        List<ShippingProfile> shippingProfiles = shippingProfileService.getShippingProfilesByCustomerUserId(customerId);
        return ApiResponseUtils.response(HttpStatus.CREATED, shippingProfiles, "Resource created successfully");
    }
}