package com.tvdgapp.repositories.shipment.pricecaculation;

import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpectedDeliveryDayRepository extends JpaRepository<ExpectedDeliveryDay, Long> {

    List<ExpectedDeliveryDay> findByShippingServiceServiceId(Integer serviceId);

    @Query("SELECT edd FROM ExpectedDeliveryDay edd JOIN edd.region r WHERE edd.shippingService.serviceId = :servicePortfolioId AND r.regionName = :regionName")
    Optional<ExpectedDeliveryDay> findByShippingServiceServiceIdAndRegionName(@Param("servicePortfolioId") Long servicePortfolioId,
                                                                        @Param("regionName") String regionName);

//    Optional<ExpectedDeliveryDay> findByServicePortfolioIdAndRegionId(Long id, Integer id1);
}

