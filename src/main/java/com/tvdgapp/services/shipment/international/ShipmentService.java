package com.tvdgapp.services.shipment.international;

import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.shipment.ShipmentStatus;
import com.tvdgapp.services.generic.TvdgEntityService;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShipmentService extends TvdgEntityService<Long, Shipment> {

    public InternationalShipmentResponseDto createShipment(ShipmentRequestDto request, MultipartFile paymentProof);

    void updateShipmentStatusById(Long id, ShipmentStatusUpdateRequestDto requestDto);

    public InternationalShipmentResponseDto createShippingRate(ShippingRateRequestDto request);

//    Collection<ListShipmentDto> listShipments(Long customerUserId);
//    Collection<ListShipmentDto> listShipmentsByRider(Long riderUserId);

//    List<Shipment> getShipmentsByCustomerUserId(Long customerUserId);
//    Page<Shipment> getAllShipments(Pageable pageable);

    Collection<ListShipmentDto> listAllShipments();

    PaginatedResponse<ListShipmentDto> getShipmentsPaginated(PaginationRequest paginationRequest);
//    Page<Shipment> getShipmentsByCustomerUserId(Long customerUserId, Pageable pageable);

    void deleteShipment(Long shipmentId);

    @Transactional
    void updateShipmentStatus(Long shipmentId, String newStatus);

    //    Shipment updateShipmentStatus(Long shipmentId, String newStatus);
//    void updateShipmentStatus(Long shipmentId, String newStatus);
//    void updateShipmentStatusById(Long shipmentId, ShipmentStatus status);
    List<ListShipmentDto> listShipmentsByStatus(String status);

//    Shipment updateShipment(Long id, ShipmentRequestDto updatedShipment);

    InternationalShipmentResponseDto getShipmentDetailById(Long shipmentId);

    ListShipmentDto trackShipment(String trackingNumber);

    public Optional<Shipment> findByTrackingNo(final String trackingNo);
    InternationalShipmentResponseDto updateShipment(UpdateShipmentRequestDto request);

//    long getShipmentsCountByUserId(Long userId);
    long getTotalShipmentsCount();
    long getShipmentsCountByStatus(String status);
    long countShipmentsByReferralCode(String referralCode);
    List<InternationalShipmentResponseDto> getShipmentsWithNullCustomerId();

}
