package com.tvdgapp.repositories.shipment;

import com.tvdgapp.dtos.shipment.ShippingProfileDto;
import com.tvdgapp.models.shipment.ShippingProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingProfileRepository extends JpaRepository<ShippingProfile, Long> {

    List<ShippingProfile> findByCustomerUserId(Long customerId);

    Optional<ShippingProfile> findByShipmentNameAndShippingModeAndCustomerUserId(String shipmentName, String shippingMode, Long customerId);

    List<ShippingProfile> findByCustomerUser_Id(Long customerUserId);
}

