package com.tvdgapp.repositories.shipment.nationwide;

import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NationWideRegionRepository extends JpaRepository<NationWideRegion, Long> {

    // In NationWideRegionRepository
    @Query("SELECT r FROM NationWideRegion r JOIN r.states s WHERE s.name = :stateName")
    List<NationWideRegion> findByStateName(@Param("stateName") String stateName);

}
