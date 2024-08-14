//package com.tvdgapp.repositories.shipment;
//
//import com.tvdgapp.models.shipment.CustomerType;
//import com.tvdgapp.models.shipment.ServiceType;
//import com.tvdgapp.models.shipment.ShippingOption;
//import com.tvdgapp.models.shipment.ShipmentType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface ShippingOptionRepository extends JpaRepository<ShippingOption, Long> {
//
//    void deleteById(Long id);
//
//    Optional<ShippingOption> findByServiceName(String serviceName);
//    List<ShippingOption> findByShipmentTypeAndCustomerType(ShipmentType shipmentType, CustomerType customerType);
//
//    boolean existsByServiceNameAndShipmentTypeAndServiceTypeAndCustomerType(
//            String serviceName,
//            ShipmentType shipmentType,
//            ServiceType serviceType,
//            CustomerType customerType
//    );
//
//
//}
