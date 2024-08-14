package com.tvdgapp.repositories.shipment.pricecaculation;

import com.tvdgapp.models.shipment.pricingcaculation.PickupState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickupStateRepository extends JpaRepository<PickupState, Long> {
    Optional<PickupState> findByStateName(String stateName);
}
