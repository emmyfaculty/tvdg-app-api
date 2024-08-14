package com.tvdgapp.repositories.wallet;

import com.tvdgapp.models.wallet.ShipmentPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentPaymentRepository extends JpaRepository<ShipmentPayment, Integer> {
    List<ShipmentPayment> findByShipmentRef(String shipmentRef);
}
