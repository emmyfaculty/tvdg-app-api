package com.tvdgapp.services.shipment.international;

import com.tvdgapp.dtos.shipment.CalculateShippingOptionDto;
import com.tvdgapp.dtos.shipment.ServicePortfolioResponseDto;
import com.tvdgapp.models.reference.Region;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.ShipmentType;
import com.tvdgapp.models.shipment.nationwide.TotalCostForWeightRange;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.models.user.customer.PricingCategory;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.reference.RegionRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ExpectedDeliveryDayRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import com.tvdgapp.services.shipment.pricingcaculation.ServicePortfolioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternationalShipmentServiceImpl implements InternationalShipmentService {

    private final ShippingServiceRepository shippingServiceRepository;
    private final CustomerUserRepository customerUserRepository;
    private final ServicePortfolioService servicePortfolioService;
    private final ExpectedDeliveryDayRepository expectedDeliveryDayRepository;

//    @Override
//    @Transactional
//    public List<ServicePortfolioResponseDto> calculateServiceOptions(CalculateShippingOptionDto shipmentRequestDto) {
//        // Calculate volumetric weight
//        double volumetricWeight = (shipmentRequestDto.getDimension().getLength() * shipmentRequestDto.getDimension().getWidth() * shipmentRequestDto.getDimension().getHeight()) / 5000.0;
//        // Determine the weight to use
//        double weightToUse = Math.max(volumetricWeight, shipmentRequestDto.getTotalShipmentWeight());
//
//        ShipmentType shipmentType = shipmentRequestDto.getShipmentType();
//
//        // Fetch the service portfolios based on the service type
//        ServiceType serviceType = shipmentRequestDto.getServiceType();
//        List<ShippingService> shippingServices = shippingServiceRepository.findByType(serviceType);
//
//        List<ServicePortfolioResponseDto> responseDtos = new ArrayList<>();
//
//        for (ShippingService shippingService : shippingServices) {
//            // Determine the regions for the provided country
//            String country = shipmentRequestDto.getCountry();
//
//            // Get customer user from security context
//            CustomerUser customerUser = getAuthenticatedCustomerUser();
//            Integer pricingLevelId = (customerUser != null) ? customerUser.getPricingLevelId() : 1;
//
//            // Fetch the appropriate price model level
//            PriceModelLevel priceModelLevel = servicePortfolioService.getPriceModelLevelForShipment(
//                            Long.valueOf(shippingService.getServiceId()), BigDecimal.valueOf(weightToUse), country, pricingLevelId, String.valueOf(shipmentRequestDto.getUnits()))
//                    .orElseThrow(() -> new IllegalArgumentException("No matching PricingModelLevel found"));
//
//            double convertedWeightToUse = convertWeightToKg(weightToUse, String.valueOf(shipmentRequestDto.getUnits()));
//
//            // Calculate total cost
//            double totalCost = calculateTotalCost(priceModelLevel, convertedWeightToUse);
//
//            String region = priceModelLevel.getRegion().getName();
//            Optional<ExpectedDeliveryDay> expectedDeliveryDayOpt = findExpectedDeliveryDayForServicePortfolioAndRegion(shippingService.getServiceId(), region);
//
//            // Fetch expected delivery days based on region and service portfolio
//
//            ServicePortfolioResponseDto dto = new ServicePortfolioResponseDto(
//                    shippingService.getServiceId(),
//                    shippingService.getServiceName(),
//                    totalCost,
//                    expectedDeliveryDayOpt.map(ExpectedDeliveryDay::getDayRange).orElse(null),
//                    shipmentType,
//                    pricingLevelId
//            );
//            responseDtos.add(dto);
//        }
//
//
//
//
//        return responseDtos;
//    }

    @Override
    @Transactional
    public List<ServicePortfolioResponseDto> calculateServiceOptions(CalculateShippingOptionDto shipmentRequestDto) {
        // Calculate volumetric weight
        double volumetricWeight = (shipmentRequestDto.getDimension().getLength()
                * shipmentRequestDto.getDimension().getWidth()
                * shipmentRequestDto.getDimension().getHeight()) / 5000.0;
        // Determine the weight to use
        double weightToUse = Math.max(volumetricWeight, shipmentRequestDto.getTotalShipmentWeight());

        ShipmentType shipmentType = shipmentRequestDto.getShipmentType();

        // Fetch the service portfolios based on the service type
        ServiceType serviceType = shipmentRequestDto.getServiceType();
        List<ShippingService> shippingServices = shippingServiceRepository.findByType(serviceType);

        // Get customer user from security context
        CustomerUser customerUser = getAuthenticatedCustomerUser();
        Integer pricingLevelId = (customerUser != null) ? customerUser.getPricingLevelId() : 1;

        String country = shipmentRequestDto.getCountry();

        return shippingServices.stream()
                .map(shippingService -> {
                    // Fetch the appropriate price model level
                    Optional<PriceModelLevel> priceModelLevelOpt = servicePortfolioService.getPriceModelLevelForShipment(
                            Long.valueOf(shippingService.getServiceId()),
                            BigDecimal.valueOf(weightToUse),
                            country,
                            pricingLevelId,
                            String.valueOf(shipmentRequestDto.getUnits())
                    );

                    if (priceModelLevelOpt.isPresent()) {
                        PriceModelLevel priceModelLevel = priceModelLevelOpt.get();

                        double convertedWeightToUse = convertWeightToKg(weightToUse,
                                String.valueOf(shipmentRequestDto.getUnits()));

                        // Calculate total cost
                        double totalCost = calculateTotalCost(priceModelLevel, convertedWeightToUse);

                        // Fetch expected delivery days based on region and service portfolio
                        String region = priceModelLevel.getRegion().getRegionName();
                        Optional<ExpectedDeliveryDay> expectedDeliveryDayOpt = findExpectedDeliveryDayForServicePortfolioAndRegion(
                                shippingService.getServiceId(), region);

                        return new ServicePortfolioResponseDto(
                                shippingService.getServiceId(),
                                shippingService.getServiceName(),
                                totalCost,
                                expectedDeliveryDayOpt.map(ExpectedDeliveryDay::getDayRange).orElse(null),
                                shipmentType,
                                pricingLevelId
                        );
                    } else {
                        // Log the service and return null if no matching PriceModelLevel is found
                        System.out.println("No matching PricingModelLevel found for service: " + shippingService.getServiceName());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }



    private Optional<ExpectedDeliveryDay> findExpectedDeliveryDayForServicePortfolioAndRegion(Integer serviceId, String region) {
        List<ExpectedDeliveryDay> expectedDeliveryDays = expectedDeliveryDayRepository.findByShippingServiceServiceId(serviceId);
        for (ExpectedDeliveryDay edd : expectedDeliveryDays) {
            if (region.equalsIgnoreCase(edd.getRegion().getRegionName())) {
                return Optional.of(edd);
            }
        }
        return Optional.empty();
    }



    private double convertWeightToKg(double weight, String units) {
        if ("lb".equalsIgnoreCase(units)) {
            return weight * 0.453592;
        } else if ("oz".equalsIgnoreCase(units)) {
            return weight * 0.0283495;
        }
        return weight;
    }

    private double calculateTotalCost(PriceModelLevel priceModelLevel, double weightToUse) {
        double pricePerKgOrTotalCost = priceModelLevel.getPrice();
        if (priceModelLevel.getTotalCostForWeightRange() == TotalCostForWeightRange.TRUE) {
            return pricePerKgOrTotalCost;
        } else {
            return pricePerKgOrTotalCost * weightToUse;
        }
    }

    private CustomerUser getAuthenticatedCustomerUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return customerUserRepository.findByEmail(userDetails.getUsername())
                    .orElse(null); // Return null if user not found in the repository
        }
        return null; // No authenticated user or not an instance of UserDetails
    }
}
