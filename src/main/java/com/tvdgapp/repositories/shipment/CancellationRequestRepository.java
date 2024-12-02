package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.CancellationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CancellationRequestRepository extends JpaRepository<CancellationRequest, Long> {
    @Query("SELECT c FROM CancellationRequest c WHERE c.shipment.shipmentRef = :shipmentRef")
    Optional<CancellationRequest> findByShipmentRef(@Param("shipmentRef") String shipmentRef);}
