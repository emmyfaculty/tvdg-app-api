package com.tvdgapp.controllers.user.rider;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.rider.RiderShipmentMapDto;
import com.tvdgapp.dtos.shipment.ListShipmentDto;
import com.tvdgapp.models.shipment.ShipmentStatus;
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
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<RiderShipmentMapDto>> assignShipmentToRider(@RequestBody RiderShipmentMapDto dto) {
        RiderShipmentMapDto assignment = riderShipmentMapService.assign(dto);
        return ApiResponseUtils.response(HttpStatus.OK, assignment, "Resource assigned successfully");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<List<ListShipmentDto>>> listAllAssignments() {
        List<ListShipmentDto> assignments = riderShipmentMapService.getAll();
        return ApiResponseUtils.response(HttpStatus.OK, assignments, "Resources retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<Optional<ListShipmentDto>>> getAssignmentById(@PathVariable Long id) {
        Optional<ListShipmentDto> responseDto = Optional.ofNullable(riderShipmentMapService.getById(id));
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<Optional<ListShipmentDto>>> getAssignmentById() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        Optional<ListShipmentDto> responseDto = Optional.ofNullable(riderShipmentMapService.getById(securedUserInfo.getUserId()));
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<RiderShipmentMapDto>> updateAssignment(@PathVariable Long id, @RequestBody RiderShipmentMapDto dto) {
        RiderShipmentMapDto responseDto = riderShipmentMapService.update(id, dto);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources updated successfully");

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<Object>> deleteAssignment(@PathVariable Long id) {
        riderShipmentMapService.delete(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }

    @GetMapping("/rider/{riderId}")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<List<ListShipmentDto>>> getAssignedShipmentsByRiderId(@PathVariable Long riderId) {
        List<ListShipmentDto> shipments = riderShipmentMapService.getAssignedShipmentsByRiderId(riderId);
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }
    @GetMapping("/rider")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<List<ListShipmentDto>>> getAssignedShipmentsByRiderId() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        List<ListShipmentDto> shipments = riderShipmentMapService.getAssignedShipmentsByRiderId(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }

    @GetMapping("/user/{userId}/shipment/{shipmentRef}")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> getShipmentByUserIdAndShipmentRef(
            @PathVariable Long userId, @PathVariable String shipmentRef) {
        ListShipmentDto shipmentDto = riderShipmentMapService.getShipmentByUserIdAndShipmentRef(userId, shipmentRef);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto, "Resources retrieved successfully");
    }
    @GetMapping("/user/shipment/{shipmentRef}")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> getShipmentByUserIdAndShipmentRef(@PathVariable String shipmentRef) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        ListShipmentDto shipmentDto = riderShipmentMapService.getShipmentByUserIdAndShipmentRef(securedUserInfo.getUserId(), shipmentRef);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto, "Resources retrieved successfully");
    }

    @GetMapping("/current-delivery")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> getCurrentDelivery() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        ListShipmentDto currentDeliveryShipment = riderShipmentMapService.getCurrentDeliveryShipment(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, currentDeliveryShipment, "Resources retrieved successfully");
    }
    @GetMapping("/{riderId}/current-delivery")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> getCurrentDelivery(@PathVariable Long riderId) {
        ListShipmentDto currentDeliveryShipment = riderShipmentMapService.getCurrentDeliveryShipment(riderId);
        return ApiResponseUtils.response(HttpStatus.OK, currentDeliveryShipment, "Resources retrieved successfully");
    }

    @PutMapping("/{riderId}/shipments/status")
    @PreAuthorize("hasAuthority('assignShipment')")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> updateShipmentStatus(
            @PathVariable Long riderId,
            @RequestParam String shipmentRef,
            @RequestParam String newStatus) {
        ListShipmentDto updatedShipment = riderShipmentMapService.updateShipmentStatus(riderId, shipmentRef, newStatus);
        return ApiResponseUtils.response(HttpStatus.OK, updatedShipment, "Resources updated successfully");
    }
    @PutMapping("/shipments/status")
    @PreAuthorize("hasAnyAuthority('manageRider', 'RIDER')")
    public ResponseEntity<ApiDataResponse<ListShipmentDto>> updateShipmentStatus(
            @RequestParam String shipmentRef,
            @RequestParam String newStatus) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        ListShipmentDto updatedShipment = riderShipmentMapService.updateShipmentStatus(securedUserInfo.getUserId(), shipmentRef, newStatus);
        return ApiResponseUtils.response(HttpStatus.OK, updatedShipment, "Resources updated successfully");
    }
}
