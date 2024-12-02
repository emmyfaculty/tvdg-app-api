package com.tvdgapp.repositories.reference;

import com.tvdgapp.models.reference.Country;
import com.tvdgapp.models.reference.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByCountryNameAndRegion(String name, Region region);
    boolean existsByCountryNameAndRegion(String name, Region region);

    Optional<Country> findByCountryName(String name);
    @Query("SELECT c FROM Country c WHERE c.countryName = :name AND c.region.regionId = :regionId")
    Optional<Country> findByCountryIdNameAndRegionId(@Param("name") String name, @Param("regionId") Long regionId);
}