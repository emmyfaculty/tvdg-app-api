package com.tvdgapp.repositories.shipment.pricecaculation;


import com.tvdgapp.models.shipment.pricingcaculation.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {
    List<PickupLocation> findByPickupRegion(String pickupRegion);
}
