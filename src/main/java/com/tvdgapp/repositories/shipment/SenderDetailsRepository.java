package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.SenderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SenderDetailsRepository extends JpaRepository<SenderDetails, Long> {
//    Optional<SenderDetails> findByShipmentId(Long shipmentId);
    @Query("SELECT sd FROM SenderDetails sd WHERE sd.id = :shipmentId")
    Optional<SenderDetails> findByShipmentId(@Param("shipmentId") Long shipmentId);
    @Query("SELECT sd FROM SenderDetails sd WHERE sd.shipmentRef = :shipmentRef")
    Optional<SenderDetails> findByShipmentRef(@Param("shipmentRef") String shipmentRef);
}
