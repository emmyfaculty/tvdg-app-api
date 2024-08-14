package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.ReceiverDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiverDetailsRepository extends JpaRepository<ReceiverDetails, Long> {
//    Optional<ReceiverDetails> findByShipmentId(Long shipmentId);
    @Query("SELECT rd FROM ReceiverDetails rd WHERE rd.id = :shipmentId")
    Optional<ReceiverDetails> findByShipmentId(@Param("shipmentId") Long shipmentId);

    @Query("SELECT rd FROM ReceiverDetails rd WHERE rd.shipmentRef = :shipmentRef")
    Optional<ReceiverDetails> findByShipmentRef(@Param("shipmentRef") String shipmentRef);
}
