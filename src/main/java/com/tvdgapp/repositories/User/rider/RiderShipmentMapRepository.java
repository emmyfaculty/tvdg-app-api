package com.tvdgapp.repositories.User.rider;

import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.user.rider.RiderShipmentMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiderShipmentMapRepository extends JpaRepository<RiderShipmentMap, Long> {
    List<RiderShipmentMap> findByRiderId(Long riderId);

    Optional<RiderShipmentMap> findByRider_IdAndShipment_ShipmentRef(Long riderId, String shipmentRef);

    @Query("SELECT rsm.shipment FROM RiderShipmentMap rsm WHERE rsm.rider.id = :riderId AND rsm.shipment.status = 'IN_TRANSIT'")
    Optional<Shipment> findCurrentDeliveryShipmentByRider(Long riderId);
    @Query("SELECT rsm FROM RiderShipmentMap rsm WHERE rsm.rider.id = :riderId AND rsm.shipment.shipmentRef = :shipmentRef")
    Optional<RiderShipmentMap> findByRiderIdAndShipmentRef(Long riderId, String shipmentRef);
}
