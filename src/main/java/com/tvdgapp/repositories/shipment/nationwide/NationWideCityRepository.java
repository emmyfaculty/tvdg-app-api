package com.tvdgapp.repositories.shipment.nationwide;

import com.tvdgapp.models.shipment.nationwide.NationWideCity;
import com.tvdgapp.models.shipment.nationwide.NationWideState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationWideCityRepository extends JpaRepository<NationWideCity, Long> {
    NationWideCity findByNameAndState(String name, NationWideState state);
}
