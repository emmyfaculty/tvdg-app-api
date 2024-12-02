package com.tvdgapp.controllers.user.rider;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.rider.RiderShipmentMapDto;
import com.tvdgapp.dtos.shipment.FetchShipmentDto;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.rider.RiderShipmentMapService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rider-shipment-map")
public class RiderShipmentMapController {

    private final RiderShipmentMapService riderShipmentMapService;
    private final UserInfoUtil userInfoUtil;

    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<RiderShipmentMapDto>> assignShipmentToRider(@RequestBody RiderShipmentMapDto dto) {
        RiderShipmentMapDto assignment = riderShipmentMapService.assign(dto);
        return ApiResponseUtils.response(HttpStatus.OK, assignment, "Resource assigned successfully");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<List<FetchShipmentDto>>> listAllAssignments() {
        List<FetchShipmentDto> assignments = riderShipmentMapService.getAll();
        return ApiResponseUtils.response(HttpStatus.OK, assignments, "Resources retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<Optional<FetchShipmentDto>>> getAssignmentById(@PathVariable Long id) {
        Optional<FetchShipmentDto> responseDto = Optional.ofNullable(riderShipmentMapService.getById(id));
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Rider:Shipment:assigned', 'RIDER')")
    public ResponseEntity<ApiDataResponse<Optional<FetchShipmentDto>>> getAssignmentById() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        Optional<FetchShipmentDto> responseDto = Optional.ofNullable(riderShipmentMapService.getById(securedUserInfo.getUserId()));
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<RiderShipmentMapDto>> updateAssignment(@PathVariable Long id, @RequestBody RiderShipmentMapDto dto) {
        RiderShipmentMapDto responseDto = riderShipmentMapService.update(id, dto);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources updated successfully");

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<Object>> deleteAssignment(@PathVariable Long id) {
        riderShipmentMapService.delete(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }

    @GetMapping("/rider/{riderId}")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<List<FetchShipmentDto>>> getAssignedShipmentsByRiderId(@PathVariable Long riderId) {
        List<FetchShipmentDto> shipments = riderShipmentMapService.getAssignedShipmentsByRiderId(riderId);
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }
    @GetMapping("/rider")
    @PreAuthorize("hasAnyAuthority('Rider:Shipment:assigned', 'RIDER')")
    public ResponseEntity<ApiDataResponse<List<FetchShipmentDto>>> getAssignedShipmentsByRiderId() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        List<FetchShipmentDto> shipments = riderShipmentMapService.getAssignedShipmentsByRiderId(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }

    @GetMapping("/user/{userId}/shipment/{shipmentRef}")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<FetchShipmentDto>> getShipmentByUserIdAndShipmentRef(
            @PathVariable Long userId, @PathVariable String shipmentRef) {
        FetchShipmentDto shipmentDto = riderShipmentMapService.getShipmentByUserIdAndShipmentRef(userId, shipmentRef);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto, "Resources retrieved successfully");
    }
    @GetMapping("/user/shipment/{shipmentRef}")
    @PreAuthorize("hasAnyAuthority('Rider:Shipment:assigned', 'RIDER')")
    public ResponseEntity<ApiDataResponse<FetchShipmentDto>> getShipmentByUserIdAndShipmentRef(@PathVariable String shipmentRef) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        FetchShipmentDto shipmentDto = riderShipmentMapService.getShipmentByUserIdAndShipmentRef(securedUserInfo.getUserId(), shipmentRef);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto, "Resources retrieved successfully");
    }

    @GetMapping("/current-delivery")
    @PreAuthorize("hasAnyAuthority('Rider:Shipment:assigned', 'RIDER')")
    public ResponseEntity<ApiDataResponse<FetchShipmentDto>> getCurrentDelivery() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        FetchShipmentDto currentDeliveryShipment = riderShipmentMapService.getCurrentDeliveryShipment(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, currentDeliveryShipment, "Resources retrieved successfully");
    }
    @GetMapping("/{riderId}/current-delivery")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<FetchShipmentDto>> getCurrentDelivery(@PathVariable Long riderId) {
        FetchShipmentDto currentDeliveryShipment = riderShipmentMapService.getCurrentDeliveryShipment(riderId);
        return ApiResponseUtils.response(HttpStatus.OK, currentDeliveryShipment, "Resources retrieved successfully");
    }

    @PutMapping("/{riderId}/shipments/status")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<FetchShipmentDto>> updateShipmentStatus(
            @PathVariable Long riderId,
            @RequestParam String shipmentRef,
            @RequestParam String newStatus) {
        FetchShipmentDto updatedShipment = riderShipmentMapService.updateShipmentStatus(riderId, shipmentRef, newStatus);
        return ApiResponseUtils.response(HttpStatus.OK, updatedShipment, "Resources updated successfully");
    }
    @PutMapping("/shipments/status")
    @PreAuthorize("hasAnyAuthority('Shipment:update:status', 'RIDER')")
    public ResponseEntity<ApiDataResponse<FetchShipmentDto>> updateShipmentStatus(
            @RequestParam String shipmentRef,
            @RequestParam String newStatus) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        FetchShipmentDto updatedShipment = riderShipmentMapService.updateShipmentStatus(securedUserInfo.getUserId(), shipmentRef, newStatus);
        return ApiResponseUtils.response(HttpStatus.OK, updatedShipment, "Resources updated successfully");
    }

    @GetMapping("/rider/shipments")
    @PreAuthorize("hasAnyAuthority('Rider:Shipment:assigned', 'RIDER')")
    public ResponseEntity<ApiDataResponse<List<FetchShipmentDto>>> getShipmentsByStatus(@RequestParam String status) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        List<FetchShipmentDto> shipmentDto =  riderShipmentMapService.getShipmentsByRiderAndStatus(securedUserInfo.getUserId(), status);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto, "Resources retrieved successfully");
    }
    @GetMapping("/rider/{riderId}/shipments")
    @PreAuthorize("hasAuthority('Shipment:assign:Rider')")
    public ResponseEntity<ApiDataResponse<List<FetchShipmentDto>>> getShipmentsByStatus(@PathVariable Long riderId, @RequestParam String status) {
        List<FetchShipmentDto> shipmentDto =  riderShipmentMapService.getShipmentsByRiderAndStatus(riderId, status);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto, "Resources retrieved successfully");
    }

    // Endpoint to check if navigation can be started
    @GetMapping("/{shipmentRef}/can-start-navigation")
    public ResponseEntity<ApiDataResponse<String>> canStartNavigation(@PathVariable String shipmentRef) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        boolean canStart = riderShipmentMapService.canStartNavigation(securedUserInfo.getUserId(), shipmentRef);
        if (canStart) {
            return ApiResponseUtils.response(HttpStatus.OK,"Navigation can be started for shipment: " + shipmentRef);
        } else {
            return ApiResponseUtils.response(HttpStatus.OK,"Cannot start navigation for shipment: " + shipmentRef);
        }
    }

    // Endpoint to get all IN_TRANSIT shipments for the rider
    @GetMapping("/in-transit")
    public ResponseEntity<ApiDataResponse<List<FetchShipmentDto>>> getInTransitShipments() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        List<FetchShipmentDto> inTransitShipments = riderShipmentMapService.getInTransitShipments(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, inTransitShipments, "Resources retrieved successfully");
    }
}
