package com.tvdgapp.repositories.reference;

import com.tvdgapp.models.reference.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByRegionName(String name);
//    boolean existsByName(String name);
}


