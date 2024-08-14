package com.tvdgapp.services.shipment.nationwide;

import com.tvdgapp.dtos.NationWideShipmentRequestDto;
import com.tvdgapp.dtos.NationWideShipmentResponseDto;
import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.exceptions.PaymentProcessingException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.populator.NationWideShipmentPopulator;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.User.admin.AdminUserRepository;
import com.tvdgapp.repositories.shipment.CustomerShipmentMapRepository;
import com.tvdgapp.repositories.shipment.PackageCategoryRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.repositories.shipment.nationwide.NationWideRegionRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PickupLocationRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PickupStateRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.shipment.ReceiverDetailsService;
import com.tvdgapp.services.shipment.SenderDetailsService;
import com.tvdgapp.services.shipment.pricingcaculation.ServicePortfolioService;
import com.tvdgapp.services.wallet.WalletService;
import com.tvdgapp.utils.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NationWideShipmentServiceImpl extends TvdgEntityServiceImpl<Long, Shipment> implements NationWideShipmentService {
    private final ShipmentRepository shipmentRepository;

    private final CustomerUserRepository customerUserRepository;

    private final AdminUserRepository adminUserRepository;
    private final PackageCategoryRepository packageCategoryRepository;
    private final SenderDetailsService senderDetailsService;
    private final ReceiverDetailsService receiverDetailsService;
    private final CustomerUserRepository repository;
    private final WalletService walletService;
    private final UserInfoUtil userInfoUtil;
    private final ShippingServiceRepository shippingServiceRepository;
    private final ServicePortfolioService servicePortfolioService;
    private final PickupStateRepository pickupStateRepository;
    private final PickupLocationRepository pickupLocationRepository;
    private final PriceModelLevelRepository priceModelLevelRepository;
    private final NationWideRegionRepository nationWideRegionRepository;
    private final CustomerShipmentMapRepository customerShipmentMapRepository;

    @Override
    @Transactional
    public NationWideShipmentResponseDto createShipment(NationWideShipmentRequestDto requestDTO) {

        Shipment shipment = this.createShipmentModelEntity(requestDTO);

        shipment = this.saveShipment(shipment);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // Authenticated user logic
            SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
            CustomerUser user = (CustomerUser) userInfo.getUser();
            this.repository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Update shipment count for the customer user
            user.setShipmentCount(user.getShipmentCount() != null ? user.getShipmentCount() + 1 : 1);
            repository.save(user);

            shipment.setPaymentMethod(PaymentMethod.WALLET);

            // Deduct the amount from the wallet
            boolean paymentSuccess = walletService.makeShipmentPayment(user.getId(), shipment.getId(), shipment.getTotalShipmentAmount());

            if (paymentSuccess) {
                shipment.setPaymentStatus(PaymentStatus.PAID); // Update payment status
                shipmentRepository.save(shipment);
            } else {
                throw new PaymentProcessingException("Payment processing failed"); // Handle payment failure scenario
            }
        } else {
            // Guest user logic
            shipment.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
            shipmentRepository.save(shipment);
        }
        // Persist the shipment object to the database
//        return ShipmentMapper.toEntity(shipment);
        return convertToResponseDTO(shipment);

    }

    private Shipment createShipmentModelEntity(NationWideShipmentRequestDto shipmentRequestDto) {
        Shipment shipment = new Shipment();
        this.populateCreateShipmentModelEntity(shipmentRequestDto, shipment);
        return shipment;
    }

    private void populateCreateShipmentModelEntity(NationWideShipmentRequestDto shipmentRequestDto, Shipment shipment) {
        NationWideShipmentPopulator shipmentPopulator = new NationWideShipmentPopulator (shippingServiceRepository, repository, packageCategoryRepository, senderDetailsService, receiverDetailsService, shipmentRepository, pickupStateRepository, pickupLocationRepository, priceModelLevelRepository,nationWideRegionRepository, customerShipmentMapRepository );
        shipmentPopulator.populate(shipmentRequestDto, shipment);
    }

    private Shipment saveShipment(Shipment shipment) {
        return this.shipmentRepository.save(shipment);
    }


//    @Override
//    public Page<NationWideShipmentResponseDto> filterShipmentsByType(ShipmentType shipmentType, Pageable pageable) {
//        Page<Shipment> shipments = shipmentRepository.findByShipmentType(shipmentType, pageable);
//        return shipments.map(this::convertToResponseDTO);
//    }
//
//    @Override
//    public Page<NationWideShipmentResponseDto> filterShipmentsByService(ServiceType serviceType, Pageable pageable) {
//        Page<Shipment> shipments = shipmentRepository.findByServiceType(serviceType, pageable);
//        return shipments.map(this::convertToResponseDTO);
//        return null;
//    }

    @Override
    @Transactional
    public NationWideShipmentResponseDto updateShipmentStatus(Long shipmentId, String status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        shipment.setStatus(status);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return convertToResponseDTO(updatedShipment);
    }

    @Override
    @Transactional
    public NationWideShipmentResponseDto assignAdminUser(Long shipmentId, Long adminUserId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        AdminUser adminUser = adminUserRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));
//        shipment.setAdminUser(adminUser);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return convertToResponseDTO(updatedShipment);
    }

    private NationWideShipmentResponseDto convertToResponseDTO(Shipment shipment) {
        NationWideShipmentResponseDto responseDto = new NationWideShipmentResponseDto();

        responseDto.setId(shipment.getId());
        responseDto.setTrackingNumber(shipment.getTrackingNumber());

        // Convert sender details
        Optional<SenderDetails> senderDetailsOpt = senderDetailsService.findByShipmentRef(shipment.getShipmentRef());
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

        // Convert receiver details
        Optional<ReceiverDetails> receiverDetailsOpt = receiverDetailsService.findByShipmentRef(shipment.getShipmentRef());
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
        responseDto.setInsurance(shipment.getInsuranceFee());
        responseDto.setVat(shipment.getVat());
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
        responseDto.setCarrier(String.valueOf(shippingService.getCarrier()));

        // Set status with null check
        String status = shipment.getStatus();
        responseDto.setShipmentStatus(status != null ? status : null);

        // Fetch the CustomerShipmentMap entry for this shipment
        // Fetch CustomerShipmentMap by shipmentRef
        Optional<CustomerShipmentMap> customerShipmentMapOpt = customerShipmentMapRepository.findByShipmentRef(shipment.getShipmentRef());
        if (customerShipmentMapOpt.isPresent()) {
            CustomerShipmentMap customerShipmentMap = customerShipmentMapOpt.get();
            responseDto.setCustomerId(customerShipmentMap.getCustomerId());

            // Fetch CustomerUser using customerId
            Optional<CustomerUser> customerUserOpt = customerUserRepository.findById(customerShipmentMap.getCustomerId());
            if (customerUserOpt.isPresent()) {
                CustomerUser customerUser = customerUserOpt.get();
                var shipmentCount = customerUser.getShipmentCount() != null ? customerUser.getShipmentCount() + 1 : null;
                responseDto.setShipmentCount(shipmentCount);
            } else {
                responseDto.setShipmentCount(null);
            }
        } else {
            responseDto.setCustomerId(null);
            responseDto.setShipmentCount(null);
        }

        responseDto.setShippedDate(String.valueOf(shipment.getShippedDate()));
        responseDto.setTransportCharge(shipment.getTransportCharge());

        // Handle expected delivery day
        if (shippingService != null && shippingService.getExpectedDeliveryDays() != null && !shippingService.getExpectedDeliveryDays().isEmpty()) {
            ExpectedDeliveryDay expectedDeliveryDay = shippingService.getExpectedDeliveryDays().get(0);
            responseDto.setEstimatedDeliveryDate(expectedDeliveryDay.getDayRange());
        } else {
            responseDto.setEstimatedDeliveryDate(null);
        }

        String paymentMethodName = (shipment.getPaymentMethod() != null) ? shipment.getPaymentMethod().name() : null;
        responseDto.setPaymentMethod(paymentMethodName);

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
