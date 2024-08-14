package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.shipment.ShipmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
//    @Query("SELECT s FROM Shipment s WHERE s.customerUser.id = :customerId")
//    List<Shipment> findByCustomerId(@Param("customerId") Long customerId);

    Shipment findByTrackingNumber(String trackingNumber);
//    Page<Shipment> findByCustomerUserId(Long customerUserId, Pageable pageable);
    @Query("SELECT s FROM Shipment s")
    List<Shipment> findAllShipments();

    List<Shipment> findByStatus(ShipmentStatus status);

    Page<Shipment> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Shipment s SET s.status = :status WHERE s.id = :id")
    int updateShipmentStatus(@Param("id") Long id, @Param("status") String status);

    @Query("SELECT COUNT(s) FROM Shipment s")
    long countTotalShipments();

//    @Query("SELECT COUNT(s) FROM Shipment s WHERE s.customerUser.id = :userId")
//    long countShipmentsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM Shipment s WHERE s.status = :status")
    long countShipmentsByStatus(@Param("status") String status);

    long countByReferralCode(String referralCode);
    List<Shipment> findAllByReferralCode(String referralCode);

//    List<Shipment> findAllByCustomerUserIdIn(List<Long> customerIds);

//    List<Shipment> findByCustomerUserIdIsNull();

    Optional<Shipment> findByShipmentRef(String shipmentRef);

    List<Shipment> findByShipmentRefIn(List<String> shipmentRefs);

    List<Shipment> findAllByShipmentRefIn(List<String> shipmentRefs);

    @Query("SELECT s.shipmentRef FROM Shipment s")
    List<String> findAllShipmentRefs();

}

