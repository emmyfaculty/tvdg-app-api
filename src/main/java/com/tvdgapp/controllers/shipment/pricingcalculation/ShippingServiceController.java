package com.tvdgapp.controllers.shipment.pricingcalculation;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.pricingcaculation.CreateShippingServiceDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.ShippingServiceResponseDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.UpdateShippingServiceDto;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.services.shipment.pricingcaculation.ShippingServiceService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipping-services")
public class ShippingServiceController {

    private final ShippingServiceService shippingServiceService;

    @PostMapping
    @PreAuthorize("hasAuthority('manageShippingService')")
    public ResponseEntity<ApiDataResponse<ShippingServiceResponseDto>> createShippingService(@RequestBody CreateShippingServiceDto dto) {
        ShippingServiceResponseDto responseDto = shippingServiceService.createShippingService(dto);
        return ApiResponseUtils.response(HttpStatus.CREATED, responseDto, "Resource created successfully");
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('manageShippingService')")
    public ResponseEntity<ApiDataResponse<ShippingServiceResponseDto>> updateShippingService(
            @PathVariable Integer serviceId,
            @RequestBody UpdateShippingServiceDto dto) {
        dto.setServiceId(serviceId);
        ShippingServiceResponseDto responseDto = shippingServiceService.updateShippingService(dto);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources updated successfully");
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('manageShippingService')")
    public ResponseEntity<ApiDataResponse<Object>> deleteShippingService(@PathVariable Integer serviceId) {
        shippingServiceService.deleteShippingService(serviceId);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");

    }

    @GetMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('manageShippingService')")
    public ResponseEntity<ApiDataResponse<ShippingServiceResponseDto>> getShippingServiceById(@PathVariable Integer serviceId) {
        ShippingServiceResponseDto responseDto = shippingServiceService.getShippingServiceById(serviceId);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<ShippingServiceResponseDto>>> getAllShippingServices() {
        List<ShippingServiceResponseDto> responseDtos = shippingServiceService.getAllShippingServices();
        return ApiResponseUtils.response(HttpStatus.OK, responseDtos, "Resources retrieved successfully");

    }

    @GetMapping("/type/{serviceType}")
    @PreAuthorize("hasAuthority('manageShippingService')")
    public ResponseEntity<ApiDataResponse<List<ShippingServiceResponseDto>>> getShippingServicesByType(@PathVariable ServiceType serviceType) {
        List<ShippingServiceResponseDto> responseDtos = shippingServiceService.getShippingServicesByType(serviceType);
        return ApiResponseUtils.response(HttpStatus.OK, responseDtos, "Resources retrieved successfully");
    }
}
