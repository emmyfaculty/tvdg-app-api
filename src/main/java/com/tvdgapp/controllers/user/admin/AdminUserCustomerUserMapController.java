package com.tvdgapp.controllers.user.admin;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.InternationalShipmentResponseDto;
import com.tvdgapp.dtos.user.admin.CustomerCseMapDTO;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.user.admin.AdminUserCustomerUserMapService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-user-customer-user-mappings")
public class AdminUserCustomerUserMapController {

    private final AdminUserCustomerUserMapService service;
    private final UserInfoUtil userInfoUtil;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<List<CustomerCseMapDTO>>> getAllMappings() {
        List<CustomerCseMapDTO> assignments = service.getAllAssignments();
        return ApiResponseUtils.response(HttpStatus.OK, assignments, "Resources retrieved successfully");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<CustomerCseMapDTO>> getMappingById(@PathVariable Long id) {
        CustomerCseMapDTO responseDto = service.getAssignmentById(id);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement', 'viewShipment')")
    public ResponseEntity<ApiDataResponse<List<CustomerCseMapDTO>>> getMappingsByCustomerId(@PathVariable Long customerId) {
        List<CustomerCseMapDTO> responseDto = service.getAssignmentsByCustomerId(customerId);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");
    }
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<List<CustomerCseMapDTO>>> getMappingsByAdminId(@PathVariable Long adminId) {
        List<CustomerCseMapDTO> responseDto = service.getAssignmentsByAdminId(adminId);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources retrieved successfully");

    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<CustomerCseMapDTO>> createMapping(@RequestBody CustomerCseMapDTO dto) {
        CustomerCseMapDTO assignment = service.createAdminAndCustomerAssignment(dto);
        return ApiResponseUtils.response(HttpStatus.OK, assignment, "Resource assigned successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<CustomerCseMapDTO>> updateMapping(@PathVariable Long id, @RequestBody CustomerCseMapDTO dto) {
        CustomerCseMapDTO responseDto = service.updateAdminAndCustomerAssignment(id, dto);
        return ApiResponseUtils.response(HttpStatus.OK, responseDto, "Resources updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<Object>> deleteMapping(@PathVariable Long id) {
        service.deleteAssignment(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }

    @GetMapping("/{adminId}/shipments")
    @PreAuthorize("hasAnyAuthority('manageShipmentAssignment', 'adminShipmentManagement')")
    public ResponseEntity<ApiDataResponse<List<InternationalShipmentResponseDto>>> getAllShipmentsByAdmin(@PathVariable Long adminId) {
        List<InternationalShipmentResponseDto> shipments = service.getAllShipmentsByAdmin(adminId);
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }
    @GetMapping("/shipments")
    @PreAuthorize("hasAuthority('viewShipment')")
    public ResponseEntity<ApiDataResponse<List<InternationalShipmentResponseDto>>> getAllShipmentsByAdmin() {
        SecuredUserInfo userInfo = (SecuredUserInfo) userInfoUtil.authenticatedUserInfo();
        List<InternationalShipmentResponseDto> shipments = service.getAllShipmentsByAdmin(userInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, shipments, "Resources retrieved successfully");
    }

}
