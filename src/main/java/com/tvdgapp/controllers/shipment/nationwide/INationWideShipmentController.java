package com.tvdgapp.controllers.shipment.nationwide;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.NationWideShipmentRequestDto;
import com.tvdgapp.dtos.NationWideShipmentResponseDto;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.ShipmentStatus;
import com.tvdgapp.models.shipment.ShipmentType;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "Nation Wide Shipment Controller" })
public interface INationWideShipmentController {

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Resource created successfully"),
            @ApiResponse(code = 400, message = "Invalid request!!!"),
            @ApiResponse(code = 409, message = "non unique entity!!!"),
            @ApiResponse(code = 401, message = "not authorized!")})
    @ApiOperation(value = "Create user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<NationWideShipmentResponseDto>> createShipment(@RequestBody NationWideShipmentRequestDto requestDTO);

//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resources retrieved successfully"),
//            @ApiResponse(code = 401, message = "not authorized!")})
//    @ApiOperation(value = "List users", notes = "authorities[manageAdminUser, viewAdminUser,manageMailingList]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Page<NationWideShipmentResponseDto>>> filterShipmentsByType(
//            @RequestParam ShipmentType shipmentType,
//            @PageableDefault(size = 10) Pageable pageable);
//
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Resources retrieved successfully"),
//            @ApiResponse(code = 401, message = "not authorized!")})
//    @ApiOperation(value = "List users", notes = "authorities[manageAdminUser, viewAdminUser,manageMailingList]", authorizations = {@Authorization(value = "JWT") })
//    ResponseEntity<ApiDataResponse<Page<NationWideShipmentResponseDto>>> filterShipmentsByService(
//            @RequestParam ServiceType serviceType,
//            @PageableDefault(size = 10) Pageable pageable);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Update admin user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<NationWideShipmentResponseDto>> updateShipmentStatus(@PathVariable Long id, @RequestParam String status);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ryder assigned successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Update admin user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<NationWideShipmentResponseDto>> assignAdminUser(@PathVariable Long id, @RequestParam Long adminUserId);
}
