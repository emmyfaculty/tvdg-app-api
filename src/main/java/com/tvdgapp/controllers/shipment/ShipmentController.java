package com.tvdgapp.controllers.shipment;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.DhlShipmentResponse;
import com.tvdgapp.dtos.dhl.ShippingRequestDto;
import com.tvdgapp.dtos.dhltrackingresponse.DhlShipmentTrackingResponse;
import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.services.DhlApiService;
import com.tvdgapp.services.DhlTrackingService;
import com.tvdgapp.services.shipment.international.ShipmentService;
import com.tvdgapp.utils.ApiResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final DhlApiService dhlApiService;
    private final DhlTrackingService dhlTrackingService;


    @PostMapping
//        @PreAuthorize("hasAuthority({'customerShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<InternationalShipmentResponseDto>> createShipment(@Valid @RequestBody ShipmentRequestDto request, @RequestPart(value = "profile_upload", required = false) MultipartFile paymentProofUpload) {
        InternationalShipmentResponseDto shipment = shipmentService.createShipment(request, paymentProofUpload);
        return ApiResponseUtils.response(HttpStatus.CREATED, shipment, "Resource created successfully");
    }
    @PostMapping("/shipping_rate")
    @PreAuthorize("hasAuthority({'customerShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<InternationalShipmentResponseDto>> createShippingRate(@Valid @RequestBody ShippingRateRequestDto request) {
        InternationalShipmentResponseDto createShippingRate = shipmentService.createShippingRate(request);
        return ApiResponseUtils.response(HttpStatus.CREATED, createShippingRate, "Resource created successfully");
    }

    @PostMapping("/{shipmentId}/status")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<Object>> updateShipmentStatusById(@PathVariable Long shipmentId,
                                                                            @RequestBody ShipmentStatusUpdateRequestDto requestDto) {
        shipmentService.updateShipmentStatusById(shipmentId, requestDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Resources updated successfully");
    }
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Void> updateShipmentStatus(@PathVariable Long id, @RequestParam ShipmentStatus status) {
//        shipmentService.updateShipmentStatusById(id, status);
//        return ResponseEntity.ok().build();
//    }

//    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasAuthority({'customerShipmentManagement'})")
//    public ResponseEntity<ApiDataResponse<Collection<ListShipmentDto>>> listShipments(@PathVariable Long userId) {
//        Collection<ListShipmentDto> list = this.shipmentService.listShipments(userId);
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//    }

//    @GetMapping("/{riderUserId}/rider")
//    public ResponseEntity<ApiDataResponse<Collection<ListShipmentDto>>> listShipmentsByRider(@PathVariable Long riderUserId) {
//        Collection<ListShipmentDto> list = this.shipmentService.listShipmentsByRider(riderUserId);
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//    }

    @GetMapping
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<Collection<ListShipmentDto>>> getAllShipments() {
        Collection<ListShipmentDto> shipments = shipmentService.listAllShipments();
        return ApiResponseUtils.response(HttpStatus.OK,shipments,"Resources retrieved successfully");
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<PaginatedResponse<ListShipmentDto>>> getShipmentsPaginated(
            @RequestParam int start,
            @RequestParam int length) {

        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setStart(start);
        paginationRequest.setLength(length);

        PaginatedResponse<ListShipmentDto> response = shipmentService.getShipmentsPaginated(paginationRequest);
        return ApiResponseUtils.response(HttpStatus.OK, response, "Shipments fetched successfully");
    }
    @GetMapping("/{shipmentId}")
    @PreAuthorize("hasAuthority({'customerShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<InternationalShipmentResponseDto>> fetchShipmentDetails(@PathVariable Long shipmentId) {
        InternationalShipmentResponseDto shipment = this.shipmentService.getShipmentDetailById(shipmentId);
        return ApiResponseUtils.response(HttpStatus.OK, shipment, "Resources retrieved successfully");
    }

    @DeleteMapping("/{shipmentId}/delete")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<String>> deleteShipment(@PathVariable Long shipmentId) throws Exception {
        this.shipmentService.deleteShipment(shipmentId);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }

//    @PutMapping("/{id}/status")
//    public ResponseEntity<ApiDataResponse<Void>> updateShipmentStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
//        shipmentService.updateShipmentStatus(id, request.getStatus());
//        return ApiResponseUtils.response(HttpStatus.OK, "Resources updated successfully");
//    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<List<ListShipmentDto>> listShipmentsByStatus(@PathVariable String status) {
        List<ListShipmentDto> shipments = shipmentService.listShipmentsByStatus(status);
        return ResponseEntity.ok(shipments);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority({'customerShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<InternationalShipmentResponseDto>> updateShipment(@Valid @RequestBody UpdateShipmentRequestDto requestDto) {
        InternationalShipmentResponseDto responseDto = shipmentService.updateShipment(requestDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, responseDto, "Resources updated successfully");
    }

    @GetMapping("/track/{trackingNumber}")
//    @PreAuthorize("hasAuthority({'manageCustomer', 'viewShipment'})")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> trackShipment(@PathVariable String trackingNumber) {
        ListShipmentDto shipment = shipmentService.trackShipment(trackingNumber);
        return ApiResponseUtils.response(HttpStatus.OK, shipment, "Resources retrieved successfully");
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<Long>> getTotalShipmentsCount() {
        long totalShipmentsCount = shipmentService.getTotalShipmentsCount();
        return ApiResponseUtils.response(HttpStatus.OK, totalShipmentsCount, "Total shipments count fetched successfully");
    }

//    @GetMapping("/count/{userId}")
//    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
//    public ResponseEntity<ApiDataResponse<Long>> getShipmentsCountByUserId(@PathVariable Long userId) {
//        long shipmentsCountByUserId = shipmentService.getShipmentsCountByUserId(userId);
//        return ApiResponseUtils.response(HttpStatus.OK, shipmentsCountByUserId, "Shipments count by user ID fetched successfully");
//    }

    @GetMapping("/count/status")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<Long>> getShipmentsCountByStatus(@RequestParam String status) {
        long shipmentsCountByStatus = shipmentService.getShipmentsCountByStatus(status);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentsCountByStatus, "Shipments count by status fetched successfully");
    }

    @GetMapping("/count/referral")
    @PreAuthorize("hasAuthority({'adminShipmentManagement'})")
    public ResponseEntity<ApiDataResponse<Object>> countShipmentsByReferralCode(@RequestParam String referralCode) {
        long count = shipmentService.countShipmentsByReferralCode(referralCode);
        return ApiResponseUtils.response(HttpStatus.OK, "Your total referred Number is " + count);
    }

    @GetMapping("/null-customer")
    public ResponseEntity<ApiDataResponse<List<InternationalShipmentResponseDto>>> getShipmentsWithNullCustomerId() {
        List<InternationalShipmentResponseDto> shipments = shipmentService.getShipmentsWithNullCustomerId();
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }

    @PostMapping("/create-shipment")
    public ResponseEntity<ApiDataResponse<DhlShipmentResponse>> createShipment(@RequestBody ShippingRequestDto shipmentRequest) throws Exception {
//        try {
            // Send the request to DHL API and get the response
            DhlShipmentResponse dhlApiResponse = dhlApiService.sendShipmentRequest(shipmentRequest);
            return ApiResponseUtils.response(HttpStatus.OK, dhlApiResponse, "Shipment created successfully");
//        } catch (Exception e) {
//            return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failed to create shipment: " + e.getMessage());
//        }
    }

    @GetMapping("/{trackingNumber}/tracking")
    public ResponseEntity<ApiDataResponse< DhlShipmentTrackingResponse>> trackShipment(@PathVariable String trackingNumber,
                                                                                       @RequestParam(value = "trackingView", defaultValue = "all-checkpoints") String trackingView,
                                                                                       @RequestParam(value = "levelOfDetail", defaultValue = "all") String levelOfDetail)
            throws IOException, InterruptedException {
        DhlShipmentTrackingResponse trackingResponse = dhlTrackingService.trackShipment(trackingNumber, trackingView, levelOfDetail);
        return ApiResponseUtils.response(HttpStatus.OK, trackingResponse);
    }

    @GetMapping("/{trackingNumber}/track-async")
    public ResponseEntity<ApiDataResponse<CompletableFuture<DhlShipmentTrackingResponse>>> trackShipmentAsync(@PathVariable String trackingNumber,
                                                                                                              @RequestParam(value = "trackingView", defaultValue = "all-checkpoints") String trackingView,
                                                                                                              @RequestParam(value = "levelOfDetail", defaultValue = "all") String levelOfDetail) {
        CompletableFuture<DhlShipmentTrackingResponse> trackingResponse = dhlTrackingService.trackShipmentAsync(trackingNumber, trackingView, levelOfDetail);
        return ApiResponseUtils.response(HttpStatus.OK, trackingResponse);
    }

}
