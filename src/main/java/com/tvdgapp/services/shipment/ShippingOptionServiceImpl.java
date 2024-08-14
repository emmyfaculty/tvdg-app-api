//package com.tvdgapp.services.shipment;
//
//import com.tvdgapp.dtos.shipment.ShippingOptionDto;
//import com.tvdgapp.exceptions.ShippingOptionNotFoundException;
//import com.tvdgapp.exceptions.TvdgException;
//import com.tvdgapp.models.shipment.CustomerType;
//import com.tvdgapp.models.shipment.ShippingOption;
//import com.tvdgapp.repositories.shipment.ShippingOptionRepository;
//import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.EnumUtils;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class ShippingOptionServiceImpl extends TvdgEntityServiceImpl<Long, ShippingOption> implements ShippingOptionService {
//
//    private final ShippingOptionRepository shippingOptionRepository;
//
//    @Override
//    public ShippingOption createShippingOption(ShippingOptionDto request) {
//
//        // Validate customerType
//        if (!EnumUtils.isValidEnum(CustomerType.class, request.getCustomerType().name())) {
//            throw new TvdgException.InvalidCustomerTypeException("Invalid customer type: " + request.getCustomerType());
//        }
//
//        // Check for duplicate shipping option
//        if (shippingOptionRepository.existsByServiceNameAndShipmentTypeAndServiceTypeAndCustomerType(
//                request.getServiceName(),
//                request.getShipmentType(),
//                request.getServiceType(),
//                request.getCustomerType()
//        )) {
//            throw new TvdgException.DuplicateEntityException("ShippingOption with the same name, service type, shipment type, and customer type already exists");
//        }
//
//        // Create and save new shipping option
//        ShippingOption shippingOption = new ShippingOption();
//        shippingOption.setServiceName(request.getServiceName());
//        shippingOption.setPricePerKG(request.getServicePrice());
//        shippingOption.setCarrier(request.getCarrier());
//        shippingOption.setPricingCategory(request.getPricingCategory());
//        shippingOption.setDeliveryDate(request.getDeliveryDate());
//        shippingOption.setShipmentType(request.getShipmentType());
//        shippingOption.setCustomerType(request.getCustomerType());
//        shippingOption.setServiceType(request.getServiceType());
//
//        return shippingOptionRepository.save(shippingOption);
//    }
//
//    @Override
//    public ShippingOption editShippingOption(Long id, ShippingOptionDto request) {
//        // Get existing service option
//        ShippingOption shippingOption = getShippingOption(id);
//        // Update fields
//        shippingOption.setServiceName(request.getServiceName());
//        shippingOption.setPricePerKG(request.getServicePrice());
//        shippingOption.setDeliveryDate(request.getDeliveryDate());
//        shippingOption.setCarrier(request.getCarrier());
//        shippingOption.setShipmentType(request.getShipmentType());
//        shippingOption.setServiceType(request.getServiceType());
//        shippingOption.setCustomerType(request.getCustomerType());
//        return shippingOptionRepository.save(shippingOption);
//    }
//
//    @Override
//    public List<ShippingOption> listShippingOptions() {
//        return shippingOptionRepository.findAll();
//    }
//
//    @Override
//    public void deleteShippingOption(Long id) {
//        shippingOptionRepository.deleteById(id);
//    }
//
//    private ShippingOption getShippingOption(Long id) {
//        return shippingOptionRepository.findById(id)
//                .orElseThrow(() -> new ShippingOptionNotFoundException("ShippingOption not found with id: " + id));
//    }
//}
