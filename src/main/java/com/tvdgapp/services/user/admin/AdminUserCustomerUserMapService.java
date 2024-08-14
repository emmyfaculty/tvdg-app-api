package com.tvdgapp.services.user.admin;

import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.dtos.user.admin.CustomerCseMapDTO;
import com.tvdgapp.exceptions.InvalidDataException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.CustomerCseMapMapper;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.admin.CustomerCseMap;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.repositories.User.admin.AdminUserCustomerUserMapRepository;
import com.tvdgapp.repositories.shipment.CustomerShipmentMapRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.services.shipment.ReceiverDetailsService;
import com.tvdgapp.services.shipment.SenderDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserCustomerUserMapService {

    private final AdminUserCustomerUserMapRepository repository;
    private final CustomerCseMapMapper mapper;
    private final UserRepository userRepository;
    private final ShipmentRepository shipmentRepository;
    private final SenderDetailsService senderDetailsService;
    private final ReceiverDetailsService receiverDetailsService;
    private final CustomerShipmentMapRepository customerShipmentMapRepository;

    private final Logger logger = LoggerFactory.getLogger(AdminUserCustomerUserMapService.class);

    public List<CustomerCseMapDTO> getAllAssignments() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerCseMapDTO getAssignmentById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id " + id));
    }

    public List<CustomerCseMapDTO> getAssignmentsByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CustomerCseMapDTO> getAssignmentsByAdminId(Long adminId) {
        return repository.findByCseId(adminId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerCseMapDTO createAdminAndCustomerAssignment(CustomerCseMapDTO dto) {

        this.validateDTO(dto);
        // Validate the assigning admin role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMERSERVICEEXECUTIVE"))) {
            throw new AccessDeniedException("Only Customer Service Executives can be assigned to customers.");
        }


        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        CustomerCseMap entity = mapper.toEntity(dto);
        entity.setAssignedBy(user.getId());
        entity.setDateCreated(LocalDateTime.now());
        entity.setTsCreated((int) (System.currentTimeMillis() / 1000));
        CustomerCseMap savedEntity = repository.save(entity);
        logger.info("Created new mapping with id {}", savedEntity.getId());
        return mapper.toDto(savedEntity);
    }

    public CustomerCseMapDTO updateAdminAndCustomerAssignment(Long id, CustomerCseMapDTO dto) {

        // Validate the assigning admin role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMERSERVICEEXECUTIVE"))) {
            throw new AccessDeniedException("Only Customer Service Executives can be assigned to customers.");
        }


        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return repository.findById(id).map(existingMapping -> {
            validateDTO(dto);

            existingMapping.setCustomerId(dto.getCustomerId());
            existingMapping.setCseId(dto.getCseId());
            existingMapping.setAssignedBy(dto.getAssignedBy());
            existingMapping.setDateModified(LocalDateTime.now());
            existingMapping.setLastModifiedBy(user.getId());
            existingMapping.setTsModified((int) (System.currentTimeMillis() / 1000));
            CustomerCseMap updatedEntity = repository.save(existingMapping);
            logger.info("Updated mapping with id {}", updatedEntity.getId());
            return mapper.toDto(updatedEntity);
        }).orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id " + id));
    }

    public void deleteAssignment(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Mapping not found with id " + id);
        }
        repository.deleteById(id);
        logger.info("Deleted mapping with id {}", id);
    }

    @Transactional
    public List<InternationalShipmentResponseDto> getAllShipmentsByAdmin(Long adminId) {
        // Validate admin exists
        validateAdmin(adminId);

        // Get all customer IDs assigned to the admin
        List<Long> customerIds = repository.findCustomerIdsByCseId(adminId);

        if (customerIds.isEmpty()) {
            throw new ResourceNotFoundException("No customers assigned to the admin");
        }

        // Get all shipment references associated with the customer IDs
        List<String> shipmentRefs = customerShipmentMapRepository.findShipmentRefsByCustomerIds(customerIds);

        if (shipmentRefs.isEmpty()) {
            throw new ResourceNotFoundException("No shipments found for the assigned customers");
        }

        // Fetch shipments using the shipment references
        List<Shipment> shipments = shipmentRepository.findAllByShipmentRefIn(shipmentRefs);

        // Map shipments to DTOs
        return shipments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    // Validate admin user
    public void validateAdmin(Long adminId) {
        // Fetch the admin user from the database
        User adminUser = userRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.error("Admin user not found with ID: {}", adminId);
                    return new ResourceNotFoundException("Admin user not found with ID: " + adminId);
                });

        // Check if the user has the "Customer Service Executive" role
        boolean hasCustomerServiceRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("CUSTOMERSERVICEEXECUTIVE"));

        if (!hasCustomerServiceRole) {
            logger.error("Admin user with ID: {} does not have the Customer Service Executive role", adminId);
            throw new AccessDeniedException("Admin user does not have the Customer Service Executive role");
        }
    }

    private void validateDTO(CustomerCseMapDTO dto) {
        if (dto.getCustomerId() == null || dto.getCseId() == null || dto.getAssignedBy() == null) {
            throw new InvalidDataException("CustomerId, CseId, and AssignedBy cannot be null.");
        }
    }

    private InternationalShipmentResponseDto convertToResponseDTO(Shipment shipment) {
        InternationalShipmentResponseDto responseDto = new InternationalShipmentResponseDto();

        responseDto.setId(shipment.getId());
        responseDto.setTrackingNumber(shipment.getTrackingNumber());

        // Convert sender details
        // Fetch SenderDetails by shipmentId
        Optional<SenderDetails> senderDetailsOpt = senderDetailsService.findByShipmentId(shipment.getId());
        if (senderDetailsOpt.isPresent()) {
            SenderDetails senderDetails = senderDetailsOpt.get();
            SenderDetailsDto senderDto = new SenderDetailsDto();
            senderDto.setShipmentRef(senderDetails.getShipmentRef());
            senderDto.setFirstName(senderDetails.getFirstName());
            senderDto.setLastName(senderDetails.getLastName());
            senderDto.setCompanyName(senderDetails.getCompanyName());
            senderDto.setCountry(senderDetails.getCountry());
            senderDto.setAddress(senderDetails.getAddress());
            senderDto.setState(senderDetails.getState());
            senderDto.setCity(senderDetails.getCity());
            senderDto.setZipcode(senderDetails.getZipcode());
            senderDto.setEmail(senderDetails.getEmail());
            senderDto.setPhoneNo(senderDetails.getPhoneNo());
            responseDto.setSenderDetails(senderDto);
        }

        // Fetch ReceiverDetails by shipmentId
        Optional<ReceiverDetails> receiverDetailsOpt = receiverDetailsService.findByShipmentId(shipment.getId());
        if (receiverDetailsOpt.isPresent()) {
            ReceiverDetails receiverDetails = receiverDetailsOpt.get();
            ReceiverDetailsDto receiverDto = new ReceiverDetailsDto();
            receiverDto.setShipmentRef(receiverDetails.getShipmentRef());
            receiverDto.setFirstName(receiverDetails.getFirstName());
            receiverDto.setLastName(receiverDetails.getLastName());
            receiverDto.setCompanyName(receiverDetails.getCompanyName());
            receiverDto.setCountry(receiverDetails.getCountry());
            receiverDto.setAddress(receiverDetails.getAddress());
            receiverDto.setState(receiverDetails.getState());
            receiverDto.setCity(receiverDetails.getCity());
            receiverDto.setZipcode(receiverDetails.getZipcode());
            receiverDto.setEmail(receiverDetails.getEmail());
            receiverDto.setPhoneNo(receiverDetails.getPhoneNo());
            responseDto.setReceiverDetails(receiverDto);
        }

        responseDto.setTotalNumberOfPackages(shipment.getTotalNumberOfPackages());
        responseDto.setTotalShipmentValue(shipment.getTotalShipmentValue());

        // Convert dimension
        Dimension dimension = shipment.getDimension();
        if (dimension != null) {
            DimensionDto dimensionDto = new DimensionDto();
            dimensionDto.setLength(dimension.getLength());
            dimensionDto.setWidth(dimension.getWidth());
            dimensionDto.setHeight(dimension.getHeight());
            responseDto.setDimension(dimensionDto);
        }

        responseDto.setTotalShipmentWeight(shipment.getTotalShipmentWeight());
        responseDto.setVolumetricWeight(shipment.getVolumetricWeight());
        responseDto.setPackagingFee(shipment.getPackagingFee());
//    responseDto.setInsuranceFee(shipment.getInsuranceFee());
        responseDto.setVat(shipment.getVat());
        responseDto.setInsurance(shipment.getInsuranceFee());
        responseDto.setTotalShipmentAmount(shipment.getTotalShipmentAmount());

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
            totalProductItemValue = totalProductItemValue.add(productItem.getValue());
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
                packageCategoryAmount = packageCategoryAmount.add(productItem.getPackageCategory().getCategoryAmount());
            }
            productItemDtos.add(productItemDto);
        }

        responseDto.setProductItems(productItemDtos);
        responseDto.setTotalProductItemsValue(totalProductItemValue);
        responseDto.setTotalProductItemsWeight(totalProductItemWeight);
        responseDto.setTotalPackageCategoryAmount(packageCategoryAmount);

        responseDto.setReferralCode(shipment.getReferralCode());

        // Set service option
        ShippingService shippingService = shipment.getShippingService();
        responseDto.setServiceOption(shippingService != null ? shippingService.getServiceName() : null);
        assert shippingService != null;
        responseDto.setCarrier(String.valueOf(shippingService.getCarrier()));

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
        if (shippingService != null && shippingService.getExpectedDeliveryDays() != null && !shippingService.getExpectedDeliveryDays().isEmpty()) {
            ExpectedDeliveryDay expectedDeliveryDay = shippingService.getExpectedDeliveryDays().get(0); // Assuming you want the first one in the list
            responseDto.setEstimatedDeliveryDate(expectedDeliveryDay.getDayRange());
        } else {
            // Handle the case where expected delivery day is not set
            responseDto.setEstimatedDeliveryDate(null);
        }

        String paymentMethodName = (shipment.getPaymentMethod() != null) ? shipment.getPaymentMethod().name() : null;
        responseDto.setPaymentMethod(paymentMethodName);
//
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
        responseDto.setVerifiedBy(shipment.getVerifiedBy() != null ? shipment.getVerifiedBy() : null);
        return responseDto;
    }

}
