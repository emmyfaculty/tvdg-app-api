//package com.tvdgapp.services.shipment.nationwide;
//
//import com.tvdgapp.dtos.NationWideShipmentRequestDto;
//import com.tvdgapp.dtos.NationWideShipmentResponseDto;
//import com.tvdgapp.models.shipment.ServiceType;
//import com.tvdgapp.models.shipment.Shipment;
//import com.tvdgapp.models.shipment.ShipmentStatus;
//import com.tvdgapp.models.shipment.ShipmentType;
//import com.tvdgapp.services.generic.TvdgEntityService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.transaction.annotation.Transactional;
//
//public interface NationWideShipmentService extends TvdgEntityService<Long, Shipment> {
//
//
////    @Transactional
//    NationWideShipmentResponseDto createShipment(NationWideShipmentRequestDto requestDTO);
//
////    Page<NationWideShipmentResponseDto> filterShipmentsByType(ShipmentType shipmentType, Pageable pageable);
////
////    Page<NationWideShipmentResponseDto> filterShipmentsByService(ServiceType serviceType, Pageable pageable);
//
//    @Transactional
//    NationWideShipmentResponseDto updateShipmentStatus(Long shipmentId, String status);
//
//    @Transactional
//    NationWideShipmentResponseDto assignAdminUser(Long shipmentId, Long adminUserId);
//}
