package com.tvdgapp.repositories.reference;

import com.tvdgapp.models.reference.Country;
import com.tvdgapp.models.reference.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByNameAndRegion(String name, Region region);
    boolean existsByNameAndRegion(String name, Region region);

    Optional<Country> findByName(String name);
    @Query("SELECT c FROM Country c WHERE c.name = :name AND c.region.regionId = :regionId")
    Optional<Country> findByNameAndRegionId(@Param("name") String name, @Param("regionId") Long regionId);
}