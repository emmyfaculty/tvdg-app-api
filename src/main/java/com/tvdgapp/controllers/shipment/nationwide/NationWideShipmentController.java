//package com.tvdgapp.controllers.shipment.nationwide;
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.NationWideShipmentRequestDto;
//import com.tvdgapp.dtos.NationWideShipmentResponseDto;
//import com.tvdgapp.dtos.common.IdResponseDto;
//import com.tvdgapp.models.shipment.ServiceType;
//import com.tvdgapp.models.shipment.ShipmentStatus;
//import com.tvdgapp.models.shipment.ShipmentType;
//import com.tvdgapp.services.shipment.nationwide.NationWideShipmentService;
//import com.tvdgapp.utils.ApiResponseUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/nation_wide/shipments")
//public class NationWideShipmentController implements INationWideShipmentController {
//
//    private final NationWideShipmentService shipmentService;
//
//    @Override
//    @PostMapping
//    public ResponseEntity<ApiDataResponse<NationWideShipmentResponseDto>> createShipment(@RequestBody NationWideShipmentRequestDto requestDTO) {
//        NationWideShipmentResponseDto responseDTO = shipmentService.createShipment(requestDTO);
//        return ApiResponseUtils.response(HttpStatus.CREATED, responseDTO, "Resource created successfully");
//
//    }
//
////    @Override
////    @GetMapping("/type")
////    public ResponseEntity<ApiDataResponse<Page<NationWideShipmentResponseDto>>> filterShipmentsByType(
////            @RequestParam ShipmentType shipmentType,
////            @PageableDefault(size = 10) Pageable pageable) {
////        Page<NationWideShipmentResponseDto> responseDTOs = shipmentService.filterShipmentsByType(shipmentType, pageable);
////        return ApiResponseUtils.response(HttpStatus.CREATED, responseDTOs, "Resource retrieved successfully");
////
////    }
////
////    @Override
////    @GetMapping("/service")
////    public ResponseEntity<ApiDataResponse<Page<NationWideShipmentResponseDto>>> filterShipmentsByService(
////            @RequestParam ServiceType serviceType,
////            @PageableDefault(size = 10) Pageable pageable) {
////        Page<NationWideShipmentResponseDto> responseDTOs = shipmentService.filterShipmentsByService(serviceType, pageable);
////        return ApiResponseUtils.response(HttpStatus.CREATED, responseDTOs, "Resource retrieved successfully");
////
////    }
//
//    @Override
//    @PutMapping("/{id}/status")
//    public ResponseEntity<ApiDataResponse<NationWideShipmentResponseDto>> updateShipmentStatus(@PathVariable Long id, @RequestParam String status) {
//        NationWideShipmentResponseDto responseDTO = shipmentService.updateShipmentStatus(id, status);
//        return ApiResponseUtils.response(HttpStatus.CREATED, responseDTO, "Resource updated successfully");
//
//    }
//
//    @Override
//    @PutMapping("/{id}/assign-admin")
//    public ResponseEntity<ApiDataResponse<NationWideShipmentResponseDto>> assignAdminUser(@PathVariable Long shipmentId, @RequestParam Long adminUserId) {
//        NationWideShipmentResponseDto responseDTO = shipmentService.assignAdminUser(shipmentId, adminUserId);
//        return ApiResponseUtils.response(HttpStatus.CREATED, responseDTO, "RyderAdminUser Assigned successfully");
//
//    }
//}
