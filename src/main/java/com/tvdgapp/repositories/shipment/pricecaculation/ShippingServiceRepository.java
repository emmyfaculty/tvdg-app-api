package com.tvdgapp.repositories.shipment.pricecaculation;

import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingServiceRepository extends JpaRepository<ShippingService, Integer> {
    List<ShippingService> findByType(ServiceType serviceType);
//    @Query("SELECT sp FROM ServicePortfolio sp WHERE SUBSTRING_INDEX(sp.serviceName, ' ', 1) = 'IMPORT'")
//    List<ServicePortfolio> findByNameStartingWithImport();
//
//    @Query("SELECT sp FROM ServicePortfolio sp WHERE SUBSTRING_INDEX(sp.serviceName, ' ', 1) = 'EXPORT'")
//    List<ServicePortfolio> findByNameStartingWithExport();

//    @Query("SELECT sp FROM ServicePortfolio sp WHERE SUBSTRING_INDEX(sp.serviceName, ' ', 1) = 'IMPORT'")
//    List<ServicePortfolio> findByNameStartingWithImport();
//
//    @Query("SELECT sp FROM ServicePortfolio sp WHERE SUBSTRING_INDEX(sp.serviceName, ' ', 1) = 'EXPORT'")
//    List<ServicePortfolio> findByNameStartingWithExport();

}