package com.tvdgapp.services.rider;

import com.tvdgapp.dtos.rider.RiderShipmentMapDto;
import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.RiderShipmentMapMapper;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.models.user.rider.RiderShipmentMap;
import com.tvdgapp.models.user.rider.RiderUser;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.repositories.User.rider.RiderRepository;
import com.tvdgapp.repositories.User.rider.RiderShipmentMapRepository;
import com.tvdgapp.repositories.reference.country.LocaleCountryRepository;
import com.tvdgapp.repositories.reference.state.StateRepository;
import com.tvdgapp.repositories.shipment.CustomerShipmentMapRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.services.auth.UserAuthServiceImpl;
import com.tvdgapp.services.shipment.ReceiverDetailsService;
import com.tvdgapp.services.shipment.SenderDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RiderShipmentMapService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);


    private final RiderShipmentMapRepository repository;

    private final RiderShipmentMapMapper mapper;

    private final RiderRepository riderRepository;

    private final ShipmentRepository shipmentRepository;

    private final UserRepository userRepository;
    private final SenderDetailsService senderDetailsService;
    private final ReceiverDetailsService receiverDetailsService;
    private final LocaleCountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CustomerShipmentMapRepository customerShipmentMapRepository;
    private final CustomerUserRepository customerUserRepository;

    public List<FetchShipmentDto> getAll() {
        List<RiderShipmentMap> assignments = repository.findAll();
        return assignments.stream()
                .map(RiderShipmentMap::getShipment) // Extract the Shipment entity
                .map(this::convertShipmentToDto) // Map the Shipment entity to ShipmentDto
                .collect(Collectors.toList());
    }

    public FetchShipmentDto getById(Long id) {
        RiderShipmentMap riderShipmentMap = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assigned shipment not found"));

        Shipment shipment = riderShipmentMap.getShipment();
        return this.convertShipmentToDto(shipment);
    }

    public FetchShipmentDto getShipmentByUserIdAndShipmentRef(Long riderId, String shipmentRef) {
        RiderShipmentMap riderShipmentMap = repository.findByRider_IdAndShipment_ShipmentRef(riderId, shipmentRef)
                .orElseThrow(() -> new RuntimeException("Shipment not found for the given userId and shipmentRef"));

        // Map the Shipment entity to ShipmentDto
        return this.convertShipmentToDto(riderShipmentMap.getShipment());
    }

    public RiderShipmentMapDto assign(RiderShipmentMapDto dto) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Get the rider and shipment
        RiderUser rider = riderRepository.findById(dto.getRiderId())
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found with id: " + dto.getRiderId()));
        Shipment shipment = shipmentRepository.findByShipmentRef(dto.getShipmentRef())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + dto.getShipmentRef()));

        // Map DTO to entity and set additional fields
        RiderShipmentMap entity = mapper.toEntity(dto);
        entity.setRider(rider);
        entity.setShipment(shipment);
        entity.setAssignedById(user.getId());
        entity.setAssignedFor(dto.getAssignedFor());
        entity.setAssignedAt(LocalDateTime.now());
        System.out.println("Assigned By ID: " + entity.getAssignedById());

        // Save and return the mapped DTO
        RiderShipmentMap savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    public RiderShipmentMapDto update(Long id, RiderShipmentMapDto dto) {
        RiderShipmentMap existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RiderShipmentMap not found with id: " + id));

        RiderUser rider = riderRepository.findById(dto.getRiderId())
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found with id: " + dto.getRiderId()));
        Shipment shipment = shipmentRepository.findByShipmentRef(dto.getShipmentRef())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + dto.getShipmentRef()));

        existingEntity.setRider(rider);
        existingEntity.setShipment(shipment);
        return mapper.toDto(repository.save(existingEntity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("RiderShipmentMap not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public List<FetchShipmentDto> getAssignedShipmentsByRiderId(Long riderId) {
        // Find all RiderShipmentMap entries for the given riderId
        List<RiderShipmentMap> assignments = repository.findByRiderId(riderId);

        // Extract shipment references
        List<String> shipmentRefs = assignments.stream()
                .map(riderShipmentMap -> riderShipmentMap.getShipment().getShipmentRef())
                .collect(Collectors.toList());

        // Find all shipments by their references
        List<Shipment> shipments = shipmentRepository.findByShipmentRefIn(shipmentRefs);

        // Map to DTOs
        return shipments.stream()
                .map(this::convertShipmentToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FetchShipmentDto getCurrentDeliveryShipment(Long riderId) {
        Shipment shipment = repository.findCurrentDeliveryShipmentByRider(riderId)
                .orElseThrow(() -> new IllegalStateException("No current delivery found for the rider."));

        // Map Shipment entity to ShipmentDto
        return convertShipmentToDto(shipment);
    }

//    @Transactional
//    public FetchShipmentDto updateShipmentStatus(Long riderId, String shipmentRef, String newStatus) {
//        // Check if the rider has any shipment in "IN_TRANSIT" status
//        Optional<Shipment> inTransitShipment = repository.findCurrentDeliveryShipmentByRider(riderId);
//
//        // If the rider already has a shipment in "IN_TRANSIT" status and the new status is also "IN_TRANSIT"
//        if (inTransitShipment.isPresent() && inTransitShipment.get().getStatus().equals("IN_TRANSIT")) {
//            throw new IllegalStateException("You cannot start a new shipment without completing the current one.");
//        }
//
//        // Fetch the shipment to update
//        Shipment shipment = shipmentRepository.findByShipmentRef(shipmentRef)
//                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
//
//        // Ensure that the shipment is assigned to the rider
//        RiderShipmentMap riderShipmentMap = repository.findByRiderIdAndShipmentRef(riderId, shipmentRef)
//                .orElseThrow(() -> new IllegalArgumentException("Shipment is not assigned to this rider"));
//
//        // Update the status of the shipment
//        shipment.setStatus(newStatus);
//        shipmentRepository.save(shipment);
//
//        return convertShipmentToDto(shipment);
//    }

//    @Transactional
//    public FetchShipmentDto updateShipmentStatus(Long riderId, String shipmentRef, String newStatus) {
//        // Fetch the shipment to update
//        Shipment shipment = shipmentRepository.findByShipmentRef(shipmentRef)
//                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
//
//        // Ensure that the shipment is assigned to the rider
//        RiderShipmentMap riderShipmentMap = repository.findByRiderIdAndShipmentRef(riderId, shipmentRef)
//                .orElseThrow(() -> new IllegalArgumentException("Shipment is not assigned to this rider"));
//
//        // Validate new status is either PICKUP or DELIVERED
//        if (!newStatus.equalsIgnoreCase("PICKUP") && !newStatus.equalsIgnoreCase("DELIVERED")) {
//            throw new IllegalArgumentException("Riders can only update the status to 'PICKUP' or 'DELIVERED'.");
//        }
//
//        // Prevent any changes to a shipment that is already delivered
//        if (shipment.getStatus().equalsIgnoreCase("DELIVERED")) {
//            throw new IllegalStateException("Shipment is already delivered. Status cannot be updated.");
//        }
//
//        shipment.setStatus(newStatus);
//        shipmentRepository.save(shipment);
//
//        return convertShipmentToDto(shipment);
//    }


    @Transactional
    public FetchShipmentDto updateShipmentStatus(Long userId, String shipmentRef, String newStatus) {
        // Fetch the shipment to update
        Shipment shipment = shipmentRepository.findByShipmentRef(shipmentRef)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        // Get the current user's role and permissions
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Set<Role> roles = user.getRoles();
        String role = roles.iterator().next().getName(); // Assuming getName() returns the role name as String
        boolean isAdmin = isAdmin(role); // Check if the user is an admin based on the role
        boolean isRider = role.equals("RIDER");
//        boolean isCustomer = role.equals("CUSTOMER");

        // Role-based validation
        if (isRider) {
            // Ensure that the shipment is assigned to the rider
            RiderShipmentMap riderShipmentMap = repository.findByRiderIdAndShipmentRef(userId, shipmentRef)
                    .orElseThrow(() -> new IllegalArgumentException("Shipment is not assigned to this rider"));

            // Riders can only update to 'PICKED_UP' or 'DELIVERED'
            if (!newStatus.equalsIgnoreCase("PICKED_UP") && !newStatus.equalsIgnoreCase("DELIVERED")) {
                throw new IllegalArgumentException("Riders can only update the status to 'PICKED_UP' or 'DELIVERED'.");
            }
        } else if (isAdmin) {
            // Admin role-based permissions
            switch (role) {
                case "MAPPING_OFFICER":
                    if (!newStatus.equalsIgnoreCase("IN_WAREHOUSE") && !newStatus.equalsIgnoreCase("IN_TRANSIT")) {
                        throw new IllegalArgumentException("Mapping Officers can only update the status to 'IN_WAREHOUSE' or 'IN_TRANSIT'.");
                    }
                    break;

                case "SORTING_OFFICER":
                    if (!newStatus.equalsIgnoreCase("SORTED")) {
                        throw new IllegalArgumentException("Sorting Officers can only update the status to 'SORTED'.");
                    }
                    break;

                case "HEAD_OF_LOCAL_DELIVERY":
                    if (!newStatus.equalsIgnoreCase("RETURNED") && !newStatus.equalsIgnoreCase("SHIPPED_OUT")) {
                        throw new IllegalArgumentException("Head of Local Delivery can only update the status to 'RETURNED' or 'SHIPPED_OUT'.");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unauthorized admin role.");
            }
        }
//        else if (isCustomer) {
//            // Customers can request cancellation only when the shipment is in the warehouse
//            if (newStatus.equalsIgnoreCase("CANCELLED") && shipment.getStatus().equalsIgnoreCase("IN_WAREHOUSE")) {
//                // Handle cancellation request approval by admin
//                createCancellationRequest(userId, shipmentRef); // Method to create a cancellation request for admin approval
//                return convertShipmentToDto(shipment); // Return without updating the status, as admin approval is needed
//            } else {
//                throw new IllegalArgumentException("Customers can only request cancellation when the shipment is in the 'IN_WAREHOUSE' state.");
//            }
//        }
        else {
            throw new IllegalArgumentException("Unauthorized user.");
        }

        // Prevent status change if the shipment is already delivered
        if (shipment.getStatus().equalsIgnoreCase("DELIVERED") && !newStatus.equalsIgnoreCase("DELIVERED")) {
            throw new IllegalStateException("Shipment is already delivered. Status cannot be updated.");
        }

        // Update the status of the shipment
        shipment.setStatus(newStatus);
        shipmentRepository.save(shipment);

        return convertShipmentToDto(shipment);
    }

    // Check if user is an admin
    private boolean isAdmin(String role) {
        return role.equalsIgnoreCase("MAPPING_OFFICER") ||
                role.equalsIgnoreCase("SORTING_OFFICER") ||
                role.equalsIgnoreCase("HEAD_OF_LOCAL_DELIVERY");
    }

//    // Method to handle customer cancellation request
//    private void createCancellationRequest(Long userId, String shipmentRef) {
//        // Implement logic to create a cancellation request for admin approval
//        // This could involve creating a new entity for CancellationRequest and saving it in the database
//        CancellationRequest request = new CancellationRequest(shipmentRef, userId, LocalDateTime.now(), CancellationStatus.PENDING);
//        cancellationRequestRepository.save(request);
//    }

    @Transactional
    public boolean canStartNavigation(Long riderId, String shipmentRef) {
        // Fetch the shipment
        Shipment shipment = shipmentRepository.findByShipmentRef(shipmentRef)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        // Ensure the shipment is assigned to the rider
        RiderShipmentMap riderShipmentMap = repository.findByRiderIdAndShipmentRef(riderId, shipmentRef)
                .orElseThrow(() -> new IllegalArgumentException("Shipment is not assigned to this rider"));

        // Check if the shipment is in IN_TRANSIT state
        if (shipment.getStatus().equalsIgnoreCase("IN_TRANSIT")) {
            logger.info("Shipment " + shipmentRef + " is in IN_TRANSIT state. Rider can start navigation.");
            return true; // Allow navigation to start
        } else {
            throw new IllegalStateException("Shipment is not in 'IN_TRANSIT' state. Navigation cannot be started.");
        }
    }

    @Transactional
    public List<FetchShipmentDto> getInTransitShipments(Long riderId) {
        // Fetch all shipments in 'IN_TRANSIT' state for the rider
        List<Shipment> inTransitShipments = repository.findShipmentsByRiderIdAndStatus(riderId, "IN_TRANSIT");

        // Convert Shipment entities to DTOs
        return inTransitShipments.stream()
                .map(this::convertShipmentToDto)
                .collect(Collectors.toList());
    }




    @Transactional(readOnly = true)
    public List<FetchShipmentDto> getShipmentsByRiderAndStatus(Long riderId, String status) {
        List<Shipment> shipments = repository.findShipmentsByRiderIdAndStatus(riderId, status);

        return shipments.stream()
                .map(this::convertShipmentToDto)
                .collect(Collectors.toList());
    }

    private FetchShipmentDto convertShipmentToDto(Shipment shipment) {
        FetchShipmentDto responseDto = new FetchShipmentDto();

        responseDto.setId(shipment.getId());

        // Fetch customerId from CustomerShipmentMap
        Optional<CustomerShipmentMap> customerShipmentMapOpt = customerShipmentMapRepository.findByShipmentRef(shipment.getShipmentRef());
        if (customerShipmentMapOpt.isPresent()) {
            CustomerShipmentMap customerShipmentMap = customerShipmentMapOpt.get();
            Long customerId = customerShipmentMap.getCustomerId();

            // Fetch customerType from Customer
            Optional<CustomerUser> customerOpt = customerUserRepository.findById(customerId);
            if (customerOpt.isPresent()) {
                CustomerUser customer = customerOpt.get();
                responseDto.setCustomerType(customer.getCustomerType());
            } else {
                responseDto.setCustomerType(null); // Handle case where customer is not found
            }
        } else {
            responseDto.setCustomerType(null); // Handle case where customerShipmentMap is not found
        }

        responseDto.setId(shipment.getId());

        // Set service option
        ShippingService shippingService = shipment.getShippingService();
        if (shippingService == null) {
            System.out.println("ShippingService is null");
        } else {
            System.out.println("ShippingService is present");
        }

        if (shippingService != null) {
            System.out.println("ShippingService Type: " + shippingService.getType());
            System.out.println("ShippingService Name: " + shippingService.getServiceName());
        }

        responseDto.setShipmentType(shippingService != null ? shippingService.getType() : null);
        responseDto.setServiceType(shippingService != null ? shippingService.getServiceName() : null);
        responseDto.setTrackingNumber(shipment.getTrackingNumber());
        responseDto.setShipmentRef(shipment.getShipmentRef());

        // Fetch SenderDetails by shipmentId
        Optional<SenderDetails> senderDetailsOpt = senderDetailsService.findByShipmentRef(shipment.getShipmentRef());
        if (senderDetailsOpt.isPresent()) {
            SenderDetails senderDetails = senderDetailsOpt.get();
            SenderDetailsDto senderDto = new SenderDetailsDto();
            senderDto.setShipmentRef(senderDetails.getShipmentRef());
            senderDto.setFirstName(senderDetails.getFirstName());
            senderDto.setLastName(senderDetails.getLastName());
            senderDto.setCompanyName(senderDetails.getCompanyName());

            // Set the country name based on the ISO2 code
            String countryCode = senderDetails.getCountry();
            LocaleCountry country = countryRepository.findByIso2(countryCode);
            senderDto.setCountry(country != null ? country.getCountryName() : countryCode);

            // Set the state name based on the state ISO2 code and country code
            String stateCode = senderDetails.getState();
            LocaleState state = stateRepository.findByIso2AndCountryCode(stateCode, countryCode);
            senderDto.setState(state != null ? state.getStateName() : stateCode);

//            senderDto.setCountry(senderDetails.getCountry());
            senderDto.setAddress(senderDetails.getAddress());
//            senderDto.setState(senderDetails.getState());
            senderDto.setCity(senderDetails.getCity());
            senderDto.setZipcode(senderDetails.getZipcode());
            senderDto.setEmail(senderDetails.getEmail());
            senderDto.setPhoneNo(senderDetails.getPhoneNo());
            responseDto.setSenderDetails(senderDto);
        }

        // Fetch ReceiverDetails by shipmentId
        Optional<ReceiverDetails> receiverDetailsOpt = receiverDetailsService.findByShipmentRef(shipment.getShipmentRef());
        if (receiverDetailsOpt.isPresent()) {
            ReceiverDetails receiverDetails = receiverDetailsOpt.get();
            ReceiverDetailsDto receiverDto = new ReceiverDetailsDto();
            receiverDto.setShipmentRef(receiverDetails.getShipmentRef());
            receiverDto.setFirstName(receiverDetails.getFirstName());
            receiverDto.setLastName(receiverDetails.getLastName());
            receiverDto.setCompanyName(receiverDetails.getCompanyName());

            // Set the country name based on the ISO2 code
            String countryCode = receiverDetails.getCountry();
            LocaleCountry country = countryRepository.findByIso2(countryCode);
            receiverDto.setCountry(country != null ? country.getCountryName() : countryCode);

            // Set the state name based on the state ISO2 code and country code
            String stateCode = receiverDetails.getState();
            LocaleState state = stateRepository.findByIso2AndCountryCode(stateCode, countryCode);
            receiverDto.setState(state != null ? state.getStateName() : stateCode);

//            receiverDto.setCountry(receiverDetails.getCountry());
            receiverDto.setAddress(receiverDetails.getAddress());
//            receiverDto.setState(receiverDetails.getState());
            receiverDto.setCity(receiverDetails.getCity());
            receiverDto.setZipcode(receiverDetails.getZipcode());
            receiverDto.setEmail(receiverDetails.getEmail());
            receiverDto.setPhoneNo(receiverDetails.getPhoneNo());
            responseDto.setReceiverDetails(receiverDto);
        }

        responseDto.setTotalNumberOfPackages(shipment.getTotalNumberOfPackages());
        responseDto.setTotalShipmentValue(shipment.getTotalShipmentValue());
        responseDto.setShipmentStatus(String.valueOf(shipment.getStatus()));
        responseDto.setPaymentStatus(String.valueOf(shipment.getPaymentStatus()));
        responseDto.setTotalShipmentWeight(shipment.getTotalShipmentWeight());
        responseDto.setTotalShipmentValue(shipment.getTotalShipmentValue());
        responseDto.setVolumetricWeight(shipment.getVolumetricWeight());
        responseDto.setPackagingFee(shipment.getPackagingFee());
//        responseDto.setServiceOption(shipment.getServiceOption().getServiceName());

        // Convert dimension
        Dimension dimension = shipment.getDimension();
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setLength(dimension.getLength());
        dimensionDto.setWidth(dimension.getWidth());
        dimensionDto.setHeight(dimension.getHeight());
        responseDto.setDimension(dimensionDto);

        // Convert product items
        // Convert product items
        Set<ProductItemDto> productItemDtos = new HashSet<>();
        BigDecimal totalProductItemValue = BigDecimal.valueOf(0.0);
        double totalProductItemWeight = 0.0;
        BigDecimal packageCategoryAmount = BigDecimal.valueOf(0.0);
        for (ProductItem productItem : shipment.getProductItems()) {
            ProductItemDto productItemDto = new ProductItemDto();
            productItemDto.setDescription(productItem.getDescription());
            productItemDto.setQuantity(productItem.getQuantity());
            productItemDto.setUnits(productItem.getUnits());
            productItemDto.setValue(productItem.getValue());
            productItemDto.setWeight(productItem.getWeight());

            // Accumulate the total value and weight
            totalProductItemValue.add(productItem.getValue()); // += productItem.getValue();
            totalProductItemWeight += productItem.getWeight();
            if (productItem.getShipment() != null) {
                productItemDto.setShipmentRef(productItem.getShipment().getShipmentRef());
            }
            if (productItem.getManufacturingCountry() != null) {
                productItemDto.setManufacturingCountry(productItem.getManufacturingCountry());
            }
            if (productItem.getPackageCategory() != null) {
                productItemDto.setPackageCategoryName(productItem.getPackageCategory().getCategoryName());
                productItemDto.setPackageCategoryAmount(productItem.getPackageCategory().getCategoryAmount());
                packageCategoryAmount.add(productItem.getPackageCategory().getCategoryAmount()); // += productItem.getPackageCategory().getCategoryDocumentAmount();
            }
            productItemDtos.add(productItemDto);
        }
        responseDto.setProductItems(productItemDtos);
        responseDto.setTotalProductItemsValue(totalProductItemValue);
        responseDto.setTotalProductItemsWeight(totalProductItemWeight);
        responseDto.setTotalPackageCategoryAmount(packageCategoryAmount);
//        responseDto.set


        responseDto.setReferralCode(shipment.getReferralCode());


// Set the service portfolio name if servicePortfolio is not null
        responseDto.setServiceName(shippingService != null ? shippingService.getServiceName() : null);

// Set the carrier if servicePortfolio is not null
        if (shippingService != null) {
            responseDto.setCarrier(String.valueOf(shippingService.getCarrier()));
        } else {
            responseDto.setCarrier(null);
        }
        responseDto.setVerificationStatus(shipment.getVerificationStatus());
        Long verifiedBy = shipment.getVerifiedBy();
        responseDto.setVerifiedBy(verifiedBy != null ? verifiedBy.toString() : null);

        // Set status with null check
        String status = shipment.getStatus();
        responseDto.setShipmentStatus(status != null ? status : null);

        // Set customer ID based on customer type
//        if (shipment.getCustomerUser() != null) {
//            responseDto.setCustomerId(shipment.getCustomerUser().getId());
//        } else {
//            responseDto.setCustomerId(null);
//        }

        responseDto.setShippedDate(String.valueOf(shipment.getShippedDate()));
        responseDto.setTransportCharge(shipment.getTransportCharge());

        // Handle expected delivery day
// Handle expected delivery day based on receiver's country and region
        ReceiverDetailsDto receiverDetailsDto = responseDto.getReceiverDetails();
        SenderDetailsDto senderDetailsDto = responseDto.getSenderDetails();

        if (shippingService != null && shippingService.getExpectedDeliveryDays() != null && !shippingService.getExpectedDeliveryDays().isEmpty()) {
            // Determine the country based on the service type (Import or Export)
            String shipmentCountry = receiverDetailsDto.getCountry();

            if (shippingService.getServiceName().toLowerCase().startsWith("import")) {
                shipmentCountry = senderDetailsDto.getCountry();
            }

            if (shipmentCountry != null) {
                // Find the matching ExpectedDeliveryDay based on the region
                String finalShipmentCountry = shipmentCountry;
                Optional<ExpectedDeliveryDay> matchingDeliveryDay = shippingService.getExpectedDeliveryDays().stream()
                        .filter(expectedDeliveryDay -> expectedDeliveryDay.getRegion().getCountries().stream()
                                .anyMatch(country -> country.getCountryName().equalsIgnoreCase(finalShipmentCountry)))
                        .findFirst();

                if (matchingDeliveryDay.isPresent()) {
                    responseDto.setEstimatedDeliveryDate(matchingDeliveryDay.get().getDayRange());
                } else {
                    // Handle the case where no matching expected delivery day is found
                    responseDto.setEstimatedDeliveryDate(null);
                }
            } else {
                // Handle the case where relevant country is not found
                responseDto.setEstimatedDeliveryDate(null);
            }
        } else {
            // Handle the case where expected delivery day is not set
            responseDto.setEstimatedDeliveryDate(null);
        }

        String paymentMethodName = (shipment.getPaymentMethod() != null) ? shipment.getPaymentMethod().name() : null;
        responseDto.setPaymentMethod(paymentMethodName);

//        if (shipment.getCustomerUser() != null) {
//            var shipmentCount = (shipment.getCustomerUser().getShipmentCount() != null) ? shipment.getCustomerUser().getShipmentCount() + 1 : null;
//            responseDto.setShipmentCount(shipmentCount);
//        } else {
//            responseDto.setShipmentCount(null);
//        }

        AuditSection auditSection = shipment.getAuditSection();
        if (auditSection != null) {
            AuditSectionDto auditSection1 = new AuditSectionDto();
            auditSection1.setCreatedAt(auditSection.getDateCreated());
            auditSection1.setModifiedAt(auditSection.getDateModified());
            auditSection1.setTsCreated(String.valueOf(auditSection.getTsCreated()));
            auditSection1.setTsModified(String.valueOf(auditSection.getTsModified()));
            responseDto.setAuditSection(auditSection1);
        }

        // Set status with null check
        com.tvdgapp.models.shipment.PaymentStatus paymentStatus = shipment.getPaymentStatus();
        responseDto.setPaymentStatus(paymentStatus != null ? paymentStatus.name() : null);

        VerificationStatus verificationStatus = shipment.getVerificationStatus();

        responseDto.setVerificationStatus(verificationStatus != null ? VerificationStatus.valueOf(verificationStatus.name()) : null);
        responseDto.setVerifiedBy(String.valueOf(shipment.getVerifiedBy() != null ? shipment.getVerifiedBy() : null));

        return responseDto;
    }

}
