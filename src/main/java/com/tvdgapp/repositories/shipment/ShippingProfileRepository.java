package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.CustomerShippingProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingProfileRepository extends JpaRepository<CustomerShippingProfile, Long> {

    List<CustomerShippingProfile> findByCustomerUserId(Long customerId);

    Optional<CustomerShippingProfile> findByShipmentNameAndShippingModeAndCustomerUserId(String shipmentName, String shippingMode, Long customerId);

    List<CustomerShippingProfile> findByCustomerUser_Id(Long customerUserId);
}

