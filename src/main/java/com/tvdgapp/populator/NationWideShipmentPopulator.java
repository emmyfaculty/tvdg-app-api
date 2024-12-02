////package com.tvdgapp.populator;
////
////import com.tvdgapp.dtos.shipment.*;
////import com.tvdgapp.exceptions.AuthenticationException;
////import com.tvdgapp.exceptions.TvdgException;
////import com.tvdgapp.models.shipment.*;
////import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
////import com.tvdgapp.models.shipment.pricingcaculation.ServicePortfolio;
////import com.tvdgapp.models.user.customer.CustomerUser;
////import com.tvdgapp.models.user.customer.PricingCategory;
////import com.tvdgapp.repositories.User.CustomerUserRepository;
////import com.tvdgapp.repositories.shipment.PackageCategoryRepository;
////import com.tvdgapp.repositories.shipment.ShipmentRepository;
////import com.tvdgapp.repositories.shipment.pricecaculation.ServicePortfolioRepository;
////import com.tvdgapp.services.shipment.ReceiverDetailsService;
////import com.tvdgapp.services.shipment.SenderDetailsService;
////import com.tvdgapp.services.shipment.pricingcaculation.ServicePortfolioService;
////import com.tvdgapp.utils.CodeGeneratorUtils;
////import jakarta.annotation.Nullable;
////import lombok.Getter;
////import lombok.Setter;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.core.userdetails.UserDetails;
////
////import java.math.BigDecimal;
////import java.util.HashSet;
////import java.util.Optional;
////import java.util.Set;
////
////
////@Getter
////@Setter
////@SuppressWarnings("NullAway.Init")
////public class ShipmentPopulator extends AbstractDataPopulator<ShipmentRequestDto, Shipment>  {
////
////    private final CustomerUserRepository repository;
////    private final ShipmentRepository shipmentRepository;
////    private final PackageCategoryRepository packageCategoryRepository;
////    private final SenderDetailsService senderDetailsService;
////    private final ReceiverDetailsService receiverDetailsService;
////    private final ServicePortfolioRepository servicePortfolioRepository;
////    private final ServicePortfolioService servicePortfolioService;
////
////
////    public ShipmentPopulator(ServicePortfolioRepository servicePortfolioRepository, CustomerUserRepository repository, PackageCategoryRepository packageCategoryRepository, SenderDetailsService senderDetailsService, ReceiverDetailsService receiverDetailsService,
////                             ShipmentRepository shipmentRepository,  ServicePortfolioService servicePortfolioService) {
////        this.servicePortfolioRepository = servicePortfolioRepository;
////        this.repository = repository;
////
////        this.packageCategoryRepository = packageCategoryRepository;
////        this.senderDetailsService = senderDetailsService;
////        this.receiverDetailsService = receiverDetailsService;
////        this.shipmentRepository = shipmentRepository;
////        this.servicePortfolioService = servicePortfolioService;
////    }
////
////    @Override
////    public Shipment populate(ShipmentRequestDto source, Shipment target) {
////
////        // Check customer type and authenticate if necessary
////        if (source.getCustomerType() != CustomerType.RETAIL) {
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
////                Optional<CustomerUser> customerUser = repository.findByEmail(userDetails.getUsername());
////                if (customerUser.isPresent()) {
////                    CustomerUser customer = customerUser.get();
////                    target.setCustomerUser(customer);
////                    target.setPricingCategory(customer.getPricingCategory()); // Set the PricingCategory from CustomerUser
////                } else {
////                    throw new AuthenticationException("Authentication failed for " + source.getCustomerType()  + " customer type");
////                }
////            } else {
////                throw new AuthenticationException("Authentication failed for " + source.getCustomerType()  + " customer type");
////            }
////        } else {
////            target.setCustomerUser(null); // Set as guest
//////            target.setPricingCategory(PricingCategory.LEVEL_1); // Set the PricingCategory to LEVEL_1 for guest users
////        }
////
////        // Set other shipment details
////        target.setCustomerType(source.getCustomerType());
////        target.setShipmentType(source.getShipmentType());
////        target.setServiceType(source.getServiceType());
////        target.setTotalNumberOfPackages(source.getTotalNumberOfPackages());
////        target.setTotalShipmentValue(source.getTotalShipmentValue());
////        target.setDimension(source.getDimension());
////        target.setTotalShipmentWeight(source.getTotalShipmentWeight());
////        target.setUnits(source.getUnits());
////        target.setReferralCode(source.getReferralCode());
////        target.setTrackingNumber(CodeGeneratorUtils.generateTrackingNumber());
////        target.setStatus(ShipmentStatus.PENDING);
////        target.setPaymentStatus(PaymentStatus.PENDING);
////        target.setVerificationStatus(VerificationStatus.PENDING);
////
////        // Calculate volumetric weight
////        double volumetricWeight = (source.getDimension().getLength() *
////                source.getDimension().getWidth() *
////                source.getDimension().getHeight()) / 5000.0;
////        target.setVolumetricWeight(volumetricWeight);
////        double weightToUse = Math.max(volumetricWeight, source.getTotalShipmentWeight());
////
////        // Calculate packaging fee
////        double packagingFee = 0.0;
////        if (source.isUseOurPackaging()) {
////            packagingFee = source.getTotalNumberOfPackages() * 3000;
////        }
////        target.setPackagingFee(packagingFee);
////
////        // Calculate total shipment amount
////        Set<ProductItem> productItems = new HashSet<>();
////        double totalCategoryDocumentAmount = 0.0;
////
////        for (ProductItemDto itemDto : source.getProductItems()) {
////            ProductItem productItem = new ProductItem();
////            productItem.setDescription(itemDto.getDescription());
////            productItem.setQuantity(itemDto.getQuantity());
////            productItem.setUnits(itemDto.getUnits());
////            productItem.setValue(itemDto.getValue());
////            productItem.setWeight(itemDto.getWeight());
////            productItem.setManufacturingCountry(itemDto.getManufacturingCountry());
////
////            PackageCategory packageCategory = packageCategoryRepository.findByCategoryName(itemDto.getPackageCategoryName())
////                    .orElseThrow(() -> new IllegalArgumentException("Package category not found"));
////            productItem.setPackageCategory(packageCategory);
////            totalCategoryDocumentAmount += packageCategory.getCategoryDocumentAmount();
////
////            // Set the shipment reference
////            productItem.setShipment(target);
////
////            productItems.add(productItem);
////        }
////        target.setProductItems(productItems);
////
////// Fetch and set PricingModelLevel
////        ServicePortfolio servicePortfolio = servicePortfolioRepository.findById(source.getServicePortfolioId())
////                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found"));
////        target.setServicePortfolio(servicePortfolio);
////        PricingCategory pricingCategory = target.getCustomerUser() != null ? target.getCustomerUser().getPricingCategory() : PricingCategory.LEVEL_1;
////
////        PriceModelLevel priceModelLevel = servicePortfolioService.getPriceModelLevelForShipment(
////                        servicePortfolio.getId(), BigDecimal.valueOf(weightToUse), source.getReceiverDetails().getRegion(), source.getReceiverDetails().getCountry(), pricingCategory, source.getUnits())
////                .orElseThrow(() -> new IllegalArgumentException("No matching PricingModelLevel found"));
////
////        // Calculate the base cost
////        double convertedWeightToUse = convertWeightToKg(weightToUse, source.getUnits());
////        double baseCost = calculateBaseCost(priceModelLevel, convertedWeightToUse);
////        target.setTransportCharge(baseCost);
////
////        double vat = baseCost * 0.075;
////        double totalShipmentAmount = baseCost + vat + packagingFee + totalCategoryDocumentAmount;
////
////        // Check if insurance is selected and partner is available
////        if (source.isInsurance()) {
////            double insuranceFee = source.getTotalShipmentValue() * 0.01;
////            totalShipmentAmount += insuranceFee;
////            target.setInsurance(insuranceFee);
////        } else {
////            target.setInsurance(0);
////        }
////
////        target.setTotalShipmentAmount(Math.round(totalShipmentAmount * 100.0) / 100.0);
////        target.setVat(vat);
////
////        // Save the shipment first to generate an ID
////        target = shipmentRepository.save(target);
////
////        // Handle sender details
////        Optional<SenderDetailsDto> senderDetailsOptional = Optional.ofNullable(source.getSenderDetails());
////        if (senderDetailsOptional.isPresent()) {
////            SenderDetailsDto senderDetailsDto = senderDetailsOptional.get();
////            SenderDetails senderDetails = new SenderDetails();
////            senderDetails.setFirstName(senderDetailsDto.getFirstName());
////            senderDetails.setLastName(senderDetailsDto.getLastName());
////            senderDetails.setCompanyName(senderDetailsDto.getCompanyName());
////            senderDetails.setCountry(senderDetailsDto.getCountry());
////            senderDetails.setAddress(senderDetailsDto.getAddress());
////            senderDetails.setState(senderDetailsDto.getState());
////            senderDetails.setCity(senderDetailsDto.getCity());
////            senderDetails.setZipcode(senderDetailsDto.getZipcode());
////            senderDetails.setEmail(senderDetailsDto.getEmail());
////            senderDetails.setPhoneNo(senderDetailsDto.getPhoneNo());
////            senderDetails.setShipmentId(target.getId());
////            senderDetailsService.saveOrUpdate(senderDetails);
////        } else {
////            throw new IllegalArgumentException("Sender details cannot be null");
////        }
////
////        // Handle receiver details
////        Optional<ReceiverDetailsDto> receiverDetailsOptional = Optional.ofNullable(source.getReceiverDetails());
////        if (receiverDetailsOptional.isPresent()) {
////            ReceiverDetailsDto receiverDetailsDto = receiverDetailsOptional.get();
////            ReceiverDetails receiverDetails = new ReceiverDetails();
////            receiverDetails.setFirstName(receiverDetailsDto.getFirstName());
////            receiverDetails.setLastName(receiverDetailsDto.getLastName());
////            receiverDetails.setCompanyName(receiverDetailsDto.getCompanyName());
////            receiverDetails.setRegion(receiverDetailsDto.getRegion());
////            receiverDetails.setCountry(receiverDetailsDto.getCountry());
////            receiverDetails.setAddress(receiverDetailsDto.getAddress());
////            receiverDetails.setState(receiverDetailsDto.getState());
////            receiverDetails.setCity(receiverDetailsDto.getCity());
////            receiverDetails.setZipcode(receiverDetailsDto.getZipcode());
////            receiverDetails.setEmail(receiverDetailsDto.getEmail());
////            receiverDetails.setPhoneNo(receiverDetailsDto.getPhoneNo());
////            receiverDetails.setShipmentId(target.getId());
////            receiverDetailsService.saveOrUpdate(receiverDetails);
////        } else {
////            throw new IllegalArgumentException("Receiver details cannot be null");
////        }
////
////        return target;
////    }
////
////    // Method to convert weight to kilograms if necessary
////    private double convertWeightToKg(double weight, Units units) {
////        return switch (units) {
////            case KG -> weight;
////            case TONS -> weight * 1000;
////            case FT -> weight * 0.453592; // Assuming FT represents pounds
////        };
////    }
////
////    // Method to calculate the base cost of the shipment
////    private double calculateBaseCost(PriceModelLevel priceModelLevel, double weightToUse) {
////        double pricePerKgOrTotalCost = priceModelLevel.getPrice();
////        return priceModelLevel.isTotalCostForWeightRange() ? pricePerKgOrTotalCost : pricePerKgOrTotalCost * weightToUse;
////    }
////    @Override
////    protected @Nullable Shipment createTarget() {
////        return null;
////    }
////}
//
//package com.tvdgapp.populator;
//
//import com.tvdgapp.dtos.NationWideShipmentRequestDto;
//import com.tvdgapp.dtos.shipment.ProductItemDto;
//import com.tvdgapp.dtos.shipment.ReceiverDetailsDto;
//import com.tvdgapp.dtos.shipment.SenderDetailsDto;
//import com.tvdgapp.exceptions.AuthenticationException;
//import com.tvdgapp.exceptions.ResourceNotFoundException;
//import com.tvdgapp.exceptions.TvdgException;
//import com.tvdgapp.models.shipment.*;
//import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
//import com.tvdgapp.models.shipment.nationwide.TotalCostForWeightRange;
//import com.tvdgapp.models.shipment.pricingcaculation.PickupLocation;
//import com.tvdgapp.models.shipment.pricingcaculation.PickupState;
//import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
//import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
//import com.tvdgapp.models.user.customer.CustomerUser;
//import com.tvdgapp.repositories.User.CustomerUserRepository;
//import com.tvdgapp.repositories.shipment.CustomerShipmentMapRepository;
//import com.tvdgapp.repositories.shipment.PackageCategoryRepository;
//import com.tvdgapp.repositories.shipment.ShipmentRepository;
//import com.tvdgapp.repositories.shipment.nationwide.NationWideRegionRepository;
//import com.tvdgapp.repositories.shipment.pricecaculation.PickupLocationRepository;
//import com.tvdgapp.repositories.shipment.pricecaculation.PickupStateRepository;
//import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;
//import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
//import com.tvdgapp.services.shipment.ReceiverDetailsService;
//import com.tvdgapp.services.shipment.SenderDetailsService;
//import com.tvdgapp.utils.CodeGeneratorUtils;
//import jakarta.annotation.Nullable;
//import lombok.Getter;
//import lombok.Setter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@Getter
//@Setter
//@SuppressWarnings("NullAway.Init")
//@Component
//public class NationWideShipmentPopulator extends AbstractDataPopulator<NationWideShipmentRequestDto, Shipment> {
//
//    private final CustomerUserRepository repository;
//    private final ShipmentRepository shipmentRepository;
//    private final PackageCategoryRepository packageCategoryRepository;
//    private final SenderDetailsService senderDetailsService;
//    private final ReceiverDetailsService receiverDetailsService;
//    private final ShippingServiceRepository shippingServiceRepository;
////    private final ServicePortfolioService servicePortfolioService;
//    private final PickupStateRepository pickupStateRepository;
//    private final PickupLocationRepository pickupLocationRepository;
//    private final PriceModelLevelRepository priceModelLevelRepository;
//    private final NationWideRegionRepository nationWideRegionRepository;
//    private final CustomerShipmentMapRepository customerShipmentMapRepository;
//
//
//    public NationWideShipmentPopulator(ShippingServiceRepository shippingServiceRepository, CustomerUserRepository repository, PackageCategoryRepository packageCategoryRepository, SenderDetailsService senderDetailsService, ReceiverDetailsService receiverDetailsService,
//                                       ShipmentRepository shipmentRepository, PickupStateRepository pickupStateRepository, PickupLocationRepository pickupLocationRepository, PriceModelLevelRepository priceModelLevelRepository,
//                                       NationWideRegionRepository nationWideRegionRepository, CustomerShipmentMapRepository customerShipmentMapRepository) {
//        this.shippingServiceRepository = shippingServiceRepository;
//        this.repository = repository;
//        this.packageCategoryRepository = packageCategoryRepository;
//        this.senderDetailsService = senderDetailsService;
//        this.receiverDetailsService = receiverDetailsService;
//        this.shipmentRepository = shipmentRepository;
////        this.servicePortfolioService = servicePortfolioService;
//        this.pickupStateRepository =pickupStateRepository;
//        this.pickupLocationRepository = pickupLocationRepository;
//        this.priceModelLevelRepository = priceModelLevelRepository;
//        this.nationWideRegionRepository = nationWideRegionRepository;
//        this.customerShipmentMapRepository = customerShipmentMapRepository;
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger(NationWideShipmentPopulator.class);
//
//
//    @Override
//    public Shipment populate(NationWideShipmentRequestDto source, Shipment target) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Check if the user is authenticated
//        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
//            // Retrieve the authenticated user's details
//            Optional<CustomerUser> customerUser = repository.findByEmail(userDetails.getUsername());
//            if (customerUser.isPresent()) {
//                CustomerUser customer = customerUser.get();
//
//                // Create a new entry in the CustomerShipmentMap
//                CustomerShipmentMap customerShipmentMap = new CustomerShipmentMap();
//                customerShipmentMap.setCustomerId(customer.getId());
//                customerShipmentMap.setShipmentRef(target.getShipmentRef());
//
//                // Save the CustomerShipmentMap entry
//                customerShipmentMapRepository.save(customerShipmentMap);
//
//                // Set the PricingLevelId from CustomerUser
//                target.setPricingLevelId(customer.getPricingLevelId());
//            } else {
//                throw new AuthenticationException("Authentication failed for authenticated customer");
//            }
//        } else {
//            // Handle guest users (not authenticated)
//            target.setPricingLevelId(null); // Set a default or guest PricingLevelId if necessary
//        }
//
//        target.setTotalNumberOfPackages(source.getTotalNumberOfPackages());
//        target.setTotalShipmentValue(source.getTotalShipmentValue());
//        target.setDimension(source.getDimension());
//        target.setTotalShipmentWeight(source.getTotalShipmentWeight());
//        target.setUnits(source.getUnits());
//        target.setReferralCode(source.getReferralCode());
//
//        if (source.getServiceId() != null) {
//            Optional<ShippingService> shippingServiceOpt = shippingServiceRepository.findById(source.getServiceId());
//            if (shippingServiceOpt.isPresent()) {
//                ShippingService shippingService = shippingServiceOpt.get();
//                if ("DLE".equalsIgnoreCase(shippingService.getCarrier().toString())) {
//                    target.setTrackingNumber(CodeGeneratorUtils.generateTrackingNumber());
//                } else {
//                    target.setTrackingNumber(null); // Set to null if carrier is not DLE
//                }
//            } else {
//                target.setTrackingNumber(null); // Set to null if shippingService is not found
//            }
//        } else {
//            target.setTrackingNumber(null); // Set to null if serviceId is null
//        }
//
//        target.setStatus("PENDING");
//        target.setPaymentStatus(PaymentStatus.PENDING);
//        target.setVerificationStatus(VerificationStatus.PENDING);
//
//        // Calculate volumetric weight
//        double volumetricWeight = (source.getDimension().getLength() *
//                source.getDimension().getWidth() *
//                source.getDimension().getHeight()) / 5000.0;
//        target.setVolumetricWeight(volumetricWeight);
//        double weightToUse = Math.max(volumetricWeight, source.getTotalShipmentWeight());
//
//        // Calculate packaging fee
//        double packagingFee = 0.0;
//        if (source.isUseOurPackaging()) {
//            packagingFee = source.getTotalNumberOfPackages() * 3000;
//        }
//        target.setPackagingFee(BigDecimal.valueOf(packagingFee));
//
//        // Calculate total shipment amount
//        Set<ProductItem> productItems = new HashSet<>();
//        BigDecimal totalCategoryDocumentAmount = BigDecimal.valueOf(0.0);
//
//        for (ProductItemDto itemDto : source.getProductItems()) {
//            ProductItem productItem = new ProductItem();
//            productItem.setDescription(itemDto.getDescription());
//            productItem.setQuantity(itemDto.getQuantity());
//            productItem.setUnits(itemDto.getUnits());
//            productItem.setValue(itemDto.getValue());
//            productItem.setWeight(itemDto.getWeight());
//            productItem.setManufacturingCountry(itemDto.getManufacturingCountry());
//
//            PackageCategory packageCategory = packageCategoryRepository.findByCategoryName(itemDto.getPackageCategoryName())
//                    .orElseThrow(() -> new IllegalArgumentException("Package category not found"));
//            productItem.setPackageCategory(packageCategory);
//            totalCategoryDocumentAmount.add(packageCategory.getCategoryAmount());
//
//            // Set the shipment reference
//            productItem.setShipment(target);
//
//            productItems.add(productItem);
//        }
//        target.setProductItems(productItems);
//
//        ShippingService shippingService = shippingServiceRepository.findById(source.getServiceId())
//                .orElseThrow(() -> new TvdgException.EntityNotFoundException("shippingService not found"));
//        target.setShippingService(shippingService);
//        Integer pricingLevelId = target.getPricingLevelId() != null ? target.getPricingLevelId() : 1;
//
//        double shipmentCost;
//        if (shippingService.getServiceName().contains("INTRA CITY")) {
//            shipmentCost = calculateIntraCitiesShipmentCost(source, BigDecimal.valueOf(weightToUse), pricingLevelId);
//        } else if (shippingService.getServiceName().contains("INTER STATE")) {
//            shipmentCost = calculateInterStateShipmentCost(source, BigDecimal.valueOf(weightToUse));
//        } else {
//            throw new RuntimeException("Invalid shipping service type");
//        }
//
//
//        target.setTransportCharge(BigDecimal.valueOf(shipmentCost));
//        BigDecimal pickupFee = BigDecimal.valueOf(0.0);
//        if (source.getPickup().name().equals("YES")) {
//            logger.info("Pickup is true, checking conditions for pickup fee");
//
//            PickupState pickupState = pickupStateRepository.findByStateName(source.getSenderDetails().getState())
//                    .orElseThrow(() -> {
//                        logger.error("No pickup region found for state {}", source.getSenderDetails().getState());
//                        return new ResourceNotFoundException("No pickup region found for state " + source.getSenderDetails().getState());
//                    });
//
//            logger.info("Found pickup region: {}", pickupState.getPickupRegion());
//
//            List<PickupLocation> pickupLocations = pickupLocationRepository.findByPickupRegion(pickupState.getPickupRegion());
//
//            if (pickupLocations.isEmpty()) {
//                logger.error("No pickup locations found for region {}", pickupState.getPickupRegion());
//                throw new ResourceNotFoundException("No pickup locations found for region " + pickupState.getPickupRegion());
//            }
//
//            logger.info("Found pickup locations: {}", pickupLocations);
//
//            for (PickupLocation pl : pickupLocations) {
//                logger.info("PickupLocation - Region: {}, WeightBandStart: {}, WeightBandEnd: {}, PickupFee: {}",
//                        pl.getPickupRegion(), pl.getWeightBandStart(), pl.getWeightBandEnd(), pl.getPickupFee());
//            }
//
//            PickupLocation pickupLocation = pickupLocations.stream()
//                    .filter(pl -> (weightToUse >= pl.getWeightBandStart() && (pl.getWeightBandEnd() == null || weightToUse <= pl.getWeightBandEnd())))
//                    .findFirst()
//                    .orElseThrow(() -> {
//                        logger.error("No matching pickup location found for region {} and weight {}", pickupState.getPickupRegion(), weightToUse);
//                        return new ResourceNotFoundException("No matching pickup location found for region " + pickupState.getPickupRegion() + " and weight " + weightToUse);
//                    });
//
//            logger.info("Found matching pickup location with pickup fee {}", pickupLocation.getPickupFee());
//            pickupFee = pickupLocation.getPickupFee();
//            target.setPickupFee(pickupFee);
////            shipment.setTotalShipmentAmount(shipment.getTotalShipmentAmount() + pickupLocation.getPickupFee());
//        } else {
//            target.setPickupFee(BigDecimal.valueOf(0));
//        }
//
//        BigDecimal vat = BigDecimal.valueOf(shipmentCost * 0.075);
////        BigDecimal totalShipmentAmount = BigDecimal.valueOf(baseCost + vat + packagingFee + totalCategoryDocumentAmount);
//        BigDecimal totalShipmentAmount = BigDecimal.valueOf(shipmentCost)
//                .add(vat)
//                .add(BigDecimal.valueOf(packagingFee))
//                .add(totalCategoryDocumentAmount)
//                .add(pickupFee);
//
//
//        // Check if insurance is selected and partner is available
//        if (source.isInsurance()) {
////            double insuranceFee = source.getTotalShipmentValue() * 0.01;
//            BigDecimal insuranceFee = source.getTotalShipmentValue().multiply(BigDecimal.valueOf(0.01));
//
//            totalShipmentAmount.add(insuranceFee);
//            target.setInsuranceFee(insuranceFee);
//        } else {
//            target.setInsuranceFee(BigDecimal.valueOf(0));
//        }
//
////        target.setTotalShipmentAmount(BigDecimal.valueOf(Math.round(totalShipmentAmount * 100.0) / 100.0));
//        BigDecimal roundedTotalShipmentAmount = totalShipmentAmount.setScale(2, RoundingMode.HALF_UP);
//        target.setTotalShipmentAmount(roundedTotalShipmentAmount);
//        target.setVat(vat);
//        target.setPickup(source.getPickup());
//        target.setPickupFee(source.getPickupFee() != null ? source.getPickupFee() : BigDecimal.ZERO);
//
//        // Save the shipment first to generate an ID
//        target = shipmentRepository.save(target);
//
//        // Handle sender details
//        Optional<SenderDetailsDto> senderDetailsOptional = Optional.ofNullable(source.getSenderDetails());
//        if (senderDetailsOptional.isPresent()) {
//            SenderDetailsDto senderDetailsDto = senderDetailsOptional.get();
//            SenderDetails senderDetails = new SenderDetails();
//            senderDetails.setFirstName(senderDetailsDto.getFirstName());
//            senderDetails.setLastName(senderDetailsDto.getLastName());
//            senderDetails.setCompanyName(senderDetailsDto.getCompanyName());
////            senderDetails.setCountry(senderDetailsDto.getCountry());
//            senderDetails.setAddress(senderDetailsDto.getAddress());
//            senderDetails.setState(senderDetailsDto.getState());
//            senderDetails.setCity(senderDetailsDto.getCity());
//            senderDetails.setZipcode(senderDetailsDto.getZipcode());
//            senderDetails.setEmail(senderDetailsDto.getEmail());
//            senderDetails.setPhoneNo(senderDetailsDto.getPhoneNo());
//            senderDetails.setShipmentRef(target.getShipmentRef());
//            senderDetailsService.saveOrUpdate(senderDetails);
//        } else {
//            throw new IllegalArgumentException("Sender details cannot be null");
//        }
//
//        // Handle receiver details
//        Optional<ReceiverDetailsDto> receiverDetailsOptional = Optional.ofNullable(source.getReceiverDetails());
//        if (receiverDetailsOptional.isPresent()) {
//            ReceiverDetailsDto receiverDetailsDto = receiverDetailsOptional.get();
//            ReceiverDetails receiverDetails = new ReceiverDetails();
//            receiverDetails.setFirstName(receiverDetailsDto.getFirstName());
//            receiverDetails.setLastName(receiverDetailsDto.getLastName());
//            receiverDetails.setCompanyName(receiverDetailsDto.getCompanyName());
////            receiverDetails.setCountry(receiverDetailsDto.getCountry());
//            receiverDetails.setAddress(receiverDetailsDto.getAddress());
//            receiverDetails.setState(receiverDetailsDto.getState());
//            receiverDetails.setCity(receiverDetailsDto.getCity());
//            receiverDetails.setZipcode(receiverDetailsDto.getZipcode());
//            receiverDetails.setEmail(receiverDetailsDto.getEmail());
//            receiverDetails.setPhoneNo(receiverDetailsDto.getPhoneNo());
//            receiverDetails.setShipmentRef(target.getShipmentRef());
//            receiverDetailsService.saveOrUpdate(receiverDetails);
//        } else {
//            throw new IllegalArgumentException("Receiver details cannot be null");
//        }
//
//        return target;
//    }
//
////    private double calculateIntraCitiesShipmentCost(NationWideShipmentRequestDto requestDto, BigDecimal weightToUse, Integer pricingLevelId) {
////        String receiverState = requestDto.getReceiverDetails().getState();
////        String receiverCity = requestDto.getReceiverDetails().getCity();
////        Long serviceId = Long.valueOf(requestDto.getServiceId());
////
////        List<NationWideRegion> regions = nationWideRegionRepository.findByStateName(receiverState);
////
////        if (regions.isEmpty()) {
////            throw new RuntimeException("Region not found");
////        }
////
////        NationWideRegion validRegion = null;
////        for (NationWideRegion region : regions) {
////            boolean cityExistsInState = region.getStates().stream()
////                    .filter(state -> state.getName().equals(receiverState))
////                    .flatMap(state -> state.getCities().stream())
////                    .anyMatch(city -> city.getName().equals(receiverCity));
////
////            if (cityExistsInState) {
////                validRegion = region;
////                break;
////            }
////        }
////
////        if (validRegion == null) {
////            throw new RuntimeException("City not found in the specified state");
////        }
////
////        List<PriceModelLevel> priceModelLevels = priceModelLevelRepository.findByNationWideRegionAndShippingService_serviceIdAndPricingLevel_levelId(validRegion, serviceId, Long.valueOf(pricingLevelId));
////
////        for (PriceModelLevel priceModelLevel : priceModelLevels) {
////            if ((weightToUse.compareTo(priceModelLevel.getWeightBandStart()) >= 0) &&
////                    (priceModelLevel.getWeightBandEnd() == null || weightToUse.compareTo(priceModelLevel.getWeightBandEnd()) <= 0)) {
////
////                if (priceModelLevel.getTotalCostForWeightRange().equals(TotalCostForWeightRange.FALSE)) {
////                    return priceModelLevel.getPrice();
////                } else {
////                    return priceModelLevel.getPrice() * weightToUse.doubleValue();
////                }
////            }
////        }
////
////        throw new RuntimeException("No matching price model level found");
////    }
//
////    private double calculateInterStateShipmentCost(NationWideShipmentRequestDto requestDto, BigDecimal weightToUse) {
////        String receiverState = requestDto.getReceiverDetails().getState();
////        String receiverCity = requestDto.getReceiverDetails().getCity();
////
////        List<NationWideRegion> regions = nationWideRegionRepository.findByStateName(receiverState);
////
////        if (regions.isEmpty()) {
////            throw new RuntimeException("Region not found");
////        }
////
////        NationWideRegion validRegion = null;
////        for (NationWideRegion region : regions) {
////            boolean cityExistsInState = region.getStates().stream()
////                    .filter(state -> state.getName().equals(receiverState))
////                    .flatMap(state -> state.getCities().stream())
////                    .anyMatch(city -> city.getName().equals(receiverCity));
////
////            if (cityExistsInState) {
////                validRegion = region;
////                break;
////            }
////        }
////
////        if (validRegion == null) {
////            throw new RuntimeException("City not found in the specified state");
////        }
////
////
////        List<PriceModelLevel> priceModelLevels = priceModelLevelRepository.findByRegion(validRegion);
////
////        for (PriceModelLevel priceModelLevel : priceModelLevels) {
////            if ((weightToUse.compareTo(priceModelLevel.getWeightBandStart()) >= 0) &&
////                    (priceModelLevel.getWeightBandEnd() == null || weightToUse.compareTo(priceModelLevel.getWeightBandEnd()) <= 0)) {
////
////                if (priceModelLevel.getTotalCostForWeightRange().equals(TotalCostForWeightRange.TRUE)) {
////                    return priceModelLevel.getPrice();
////                } else {
////                    return priceModelLevel.getPrice() * weightToUse.doubleValue();
////                }
////            }
////        }
////
////        throw new RuntimeException("No matching price model level found");
////    }
//
//
//    @Nullable
//    @Override
//    protected Shipment createTarget() {
//        return null;
//    }
//}
//
//
