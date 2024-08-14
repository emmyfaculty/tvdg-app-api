package com.tvdgapp.repositories.shipment.pricecaculation;

import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingLevelRepository extends JpaRepository<PricingLevel, Integer> {
    Optional<PricingLevel> findById(Integer id);
}
