package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.CustomerShipmentMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerShipmentMapRepository extends JpaRepository<CustomerShipmentMap, Long> {
    // Custom query methods if needed
    List<CustomerShipmentMap> findByCustomerId(Long customerId);
    Optional<CustomerShipmentMap> findByCustomerIdAndShipmentRef(Long customerId, String shipmentRef);

    Optional<CustomerShipmentMap> findByShipmentRef(String shipmentRef);

    @Query("SELECT csm.shipmentRef FROM CustomerShipmentMap csm WHERE csm.customerId IN :customerIds")
    List<String> findShipmentRefsByCustomerIds(@Param("customerIds") List<Long> customerIds);

    @Query("SELECT csm.shipmentRef FROM CustomerShipmentMap csm")
    List<String> findAllShipmentRefs();
}
