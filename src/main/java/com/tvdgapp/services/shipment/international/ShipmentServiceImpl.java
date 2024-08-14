package com.tvdgapp.services.shipment.international;

import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.exceptions.PaymentProcessingException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.exceptions.ShipmentNotFoundException;
import com.tvdgapp.exceptions.TvdgException;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.models.wallet.ShipmentPayment;
import com.tvdgapp.populator.ShipmentPopulator;
import com.tvdgapp.populator.ShippingRatePopulator;
import com.tvdgapp.populator.UpdateShipmentPopulator;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.shipment.CustomerShipmentMapRepository;
import com.tvdgapp.repositories.shipment.PackageCategoryRepository;
import com.tvdgapp.repositories.shipment.ProductItemRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PickupLocationRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PickupStateRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import com.tvdgapp.repositories.wallet.ShipmentPaymentRepository;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.affiliate.AffiliateUserService;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.models.shipment.PaymentStatus;
import com.tvdgapp.services.shipment.ReceiverDetailsService;
import com.tvdgapp.services.shipment.SenderDetailsService;
import com.tvdgapp.services.shipment.pricingcaculation.ServicePortfolioService;
import com.tvdgapp.services.storage.FileStorageService;
import com.tvdgapp.services.wallet.WalletService;
import com.tvdgapp.utils.FilePathUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.UserInfoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tvdgapp.models.shipment.ShipmentStatus;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentServiceImpl extends TvdgEntityServiceImpl<Long, Shipment> implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final FileStorageService fileStorageService;
    private final PackageCategoryRepository packageCategoryRepository;
    private final CustomerUserRepository repository;
    private final AffiliateUserService affiliateUserService;
    private final WalletService walletService;
    private final UserInfoUtil userInfoUtil;
    private final SenderDetailsService senderDetailsService;
    private final ReceiverDetailsService receiverDetailsService;
    private final ShipmentPaymentRepository shipmentPaymentRepository;
    private final ProductItemRepository productItemRepository;
    private final ShippingServiceRepository shippingServiceRepository;
    private final ServicePortfolioService servicePortfolioService;
    private final PickupStateRepository pickupStateRepository;
    private final PickupLocationRepository pickupLocationRepository;
    private  final CustomerShipmentMapRepository customerShipmentMapRepository;
    @Transactional
    @Override
    public InternationalShipmentResponseDto createShipment(ShipmentRequestDto request, MultipartFile paymentProof) {
        Shipment shipment = this.createShipmentModelEntity(request);

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


        //todo:process asynchronously
        this.storeFiles(shipment, paymentProof);

        // Persist the shipment object to the database
//        return ShipmentMapper.toEntity(shipment);
        return convertToResponseDTO(shipment);

    }
    @Override
    public InternationalShipmentResponseDto updateShipment(UpdateShipmentRequestDto request) {
        Shipment shipment = this.updateShipmentModelEntity(request);

        shipment = this.saveShipment(shipment);

        // Persist the shipment object to the database
        return convertToResponseDTO(shipment);

    }
    @Override
    public void updateShipmentStatusById(Long id, String status) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        shipment.setStatus(status);

        shipmentRepository.save(shipment);

        if (status.equals(ShipmentStatus.COMPLETED.name()) && shipment.getReferralCode() != null && !shipment.getReferralCode().isEmpty()) {
            affiliateUserService.processReferral(id);
        }

    }
    @Override
    public InternationalShipmentResponseDto createShippingRate(ShippingRateRequestDto request) {
        Shipment shipment = this.createShippingRateModelEntity(request);

//        shipment = this.saveShipment(shipment);
        return convertToResponseDTO(shipment);

    }

//    @Override
//    @Transactional
//    public Collection<ListShipmentDto> listShipments(Long customerUserId) {
//        Collection<Shipment> shipments = shipmentRepository.findByCustomerId(customerUserId);
//        if(shipments.isEmpty()){
//            return Collections.emptyList();
//        }
//        return shipments.stream()
//                .map(this::convertShipmentToDto)
//                .collect(Collectors.toList());
//
//    }

    @Override
    @Transactional
    public PaginatedResponse<ListShipmentDto> getShipmentsPaginated(PaginationRequest paginationRequest) {
        int page = paginationRequest.getStart() / paginationRequest.getLength();
        Pageable pageable = PageRequest.of(page, paginationRequest.getLength());

        Page<Shipment> shipmentsPage = shipmentRepository.findAll(pageable);

        List<ListShipmentDto> shipments = shipmentsPage.getContent().stream()
                .map(this::convertShipmentToDto)
                .collect(Collectors.toList());

        PaginatedResponse<ListShipmentDto> response = new PaginatedResponse<>();
        response.setData(shipments);
        response.setRecordsTotal((int) shipmentRepository.count());
        response.setRecordsFiltered((int) shipmentsPage.getTotalElements());

        return response;
    }

    @Override
    @Transactional
    public Collection<ListShipmentDto> listAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAllShipments();
        return shipments.stream().map(this::convertShipmentToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("Shipment not found with id: " + shipmentId));
        shipmentRepository.delete(shipment);
    }

    @Override
    @Transactional
    public void updateShipmentStatus(Long shipmentId, String newStatus) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("Shipment not found with id: " + shipmentId));

        shipment.setStatus(newStatus);
        shipmentRepository.save(shipment);
    }

    @Override
    @Transactional
    public List<ListShipmentDto> listShipmentsByStatus(String status) {
        com.tvdgapp.models.shipment.ShipmentStatus statusEnum = com.tvdgapp.models.shipment.ShipmentStatus.valueOf(status.toUpperCase());
        List<Shipment> shipments = shipmentRepository.findByStatus(statusEnum);

        // Convert to DTOs and ensure lazy fields are initialized
        return shipments.stream()
                .map(this::convertShipmentToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InternationalShipmentResponseDto getShipmentDetailById(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with ID: " + shipmentId));
        InternationalShipmentResponseDto response = convertToResponseDTO(shipment);

        // Fetch payment details if payment status is PAID
        if (shipment.getPaymentStatus() == PaymentStatus.PAID) {
            List<ShipmentPayment> payments = shipmentPaymentRepository.findByShipmentRef(String.valueOf(shipment.getId()));
            response.setShipmentPayments(payments);
        }

        // Calculate total product items value and weight
        BigDecimal totalValue = BigDecimal.valueOf(productItemRepository.sumTotalItemValueByShipmentId(shipment.getId()));
        double totalWeight = productItemRepository.sumTotalItemWeightByShipmentId(shipment.getId());
        response.setTotalProductItemsValue(totalValue);
        response.setTotalProductItemsWeight(totalWeight);

        return response;
    }

    public long getTotalShipmentsCount() {
        return shipmentRepository.countTotalShipments();
    }
//    @Override
//    public long getShipmentsCountByUserId(Long userId) {
//        return shipmentRepository.countShipmentsByUserId(userId);
//    }
    @Override
    public long getShipmentsCountByStatus(String status) {
        return shipmentRepository.countShipmentsByStatus(status);
    }

    public long countShipmentsByReferralCode(String referralCode) {
        return shipmentRepository.countByReferralCode(referralCode);
    }
    private InternationalShipmentResponseDto convertToResponseDTO(Shipment shipment) {
        InternationalShipmentResponseDto responseDto = new InternationalShipmentResponseDto();
        ShipmentRequestDto shipmentRequestDto = new ShipmentRequestDto();

        responseDto.setId(shipment.getId());
        responseDto.setTrackingNumber(shipment.getTrackingNumber());

        // Convert sender details
        // Fetch SenderDetails by shipmentId
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

        // Fetch ReceiverDetails by shipmentId
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
        responseDto.setCarrier(String.valueOf(shippingService.getCarrier()));

        // Set status with null check
        String status = shipment.getStatus();
        responseDto.setShipmentStatus(status != null ? status : null);

        // Fetch CustomerShipmentMap by shipmentRef
        Optional<CustomerShipmentMap> customerShipmentMapOpt = customerShipmentMapRepository.findByShipmentRef(shipment.getShipmentRef());
        if (customerShipmentMapOpt.isPresent()) {
            CustomerShipmentMap customerShipmentMap = customerShipmentMapOpt.get();
            responseDto.setCustomerId(customerShipmentMap.getCustomerId());

            // Fetch CustomerUser using customerId
            Optional<CustomerUser> customerUserOpt = repository.findById(customerShipmentMap.getCustomerId());
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
            ExpectedDeliveryDay expectedDeliveryDay = shippingService.getExpectedDeliveryDays().get(0); // Assuming you want the first one in the list
            responseDto.setEstimatedDeliveryDate(expectedDeliveryDay.getDayRange());
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
        responseDto.setVerifiedBy(shipment.getVerifiedBy() != null ? shipment.getVerifiedBy() : null);
        return responseDto;
    }

    private ShipmentDetailDto convertShipmentDetailsToDto(Shipment shipment) {
        ShipmentDetailDto responseDto = new ShipmentDetailDto();

        responseDto.setId(shipment.getId());
        responseDto.setTrackingNumber(shipment.getTrackingNumber());
        // Fetch SenderDetails by shipmentId
        Optional<SenderDetails> senderDetailsOpt = senderDetailsService.findByShipmentRef(shipment.getShipmentRef());
        if (senderDetailsOpt.isPresent()) {
            SenderDetails senderDetails = senderDetailsOpt.get();
            SenderDetailsDto senderDto = new SenderDetailsDto();
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
        Optional<ReceiverDetails> receiverDetailsOpt = receiverDetailsService.findByShipmentRef(shipment.getShipmentRef());
        if (receiverDetailsOpt.isPresent()) {
            ReceiverDetails receiverDetails = receiverDetailsOpt.get();
            ReceiverDetailsDto receiverDto = new ReceiverDetailsDto();
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
        responseDto.setStatus(String.valueOf(shipment.getStatus()));
        responseDto.setTotalShipmentWeight(shipment.getTotalShipmentWeight());
        responseDto.setTotalShipmentValue(shipment.getTotalShipmentValue());
        responseDto.setVolumetricWeight(shipment.getVolumetricWeight());
        responseDto.setTotalNumberOfPackages(shipment.getTotalNumberOfPackages());
//        responseDto.setServiceOption(shipment.getServiceOption().getServiceName());

        // Convert dimension
        Dimension dimension = shipment.getDimension();
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setLength(dimension.getLength());
        dimensionDto.setWidth(dimension.getWidth());
        dimensionDto.setHeight(dimension.getHeight());
        responseDto.setDimension(dimensionDto);

        // Convert product items
        Set<ProductItemDto> productItemDtos = new HashSet<>();
        for (ProductItem productItem : shipment.getProductItems()) {
            ProductItemDto productItemDto = new ProductItemDto();
            productItemDto.setDescription(productItem.getDescription());
            productItemDto.setQuantity(productItem.getQuantity());
            productItemDto.setUnits(productItem.getUnits());
            productItemDto.setValue(productItem.getValue());
            productItemDto.setWeight(productItem.getWeight());
            productItemDto.setShipmentRef(productItem.getShipment().getShipmentRef());
            productItemDto.setManufacturingCountry(productItem.getManufacturingCountry());
            productItemDto.setPackageCategoryName(productItem.getPackageCategory().getCategoryName());
            productItemDtos.add(productItemDto);
        }
        responseDto.setProductItems(productItemDtos);

        responseDto.setReferralCode(shipment.getReferralCode());

        // Set service option
        ShippingService shippingService = shipment.getShippingService();
        responseDto.setShippingOption(shippingService != null ? shippingService.getServiceName() : null);
        responseDto.setCarrier(String.valueOf(shippingService.getCarrier()));

        // Set status with null check
        String status = shipment.getStatus();
        responseDto.setStatus(status != null ? status : null);
        responseDto.setShippedDate(String.valueOf(shipment.getShippedDate()));

        return responseDto;
    }


    private ListShipmentDto convertShipmentToDto(Shipment shipment) {
        ListShipmentDto responseDto = new ListShipmentDto();

        responseDto.setId(shipment.getId());
        responseDto.setTrackingNumber(shipment.getTrackingNumber());

        // Fetch SenderDetails by shipmentId
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

        // Fetch ReceiverDetails by shipmentId
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

        // Set service option
        ShippingService shippingService = shipment.getShippingService();

// Set the service portfolio name if servicePortfolio is not null
        responseDto.setServicePortfolio(shippingService != null ? shippingService.getServiceName() : null);

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

//        // Set customer ID based on customer type
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
                                .anyMatch(country -> country.getName().equalsIgnoreCase(finalShipmentCountry)))
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

        // Fetch CustomerShipmentMap by shipmentRef
        Optional<CustomerShipmentMap> customerShipmentMapOpt = customerShipmentMapRepository.findByShipmentRef(shipment.getShipmentRef());
        if (customerShipmentMapOpt.isPresent()) {
            CustomerShipmentMap customerShipmentMap = customerShipmentMapOpt.get();
            responseDto.setCustomerId(customerShipmentMap.getCustomerId());

            // Fetch CustomerUser using customerId
            Optional<CustomerUser> customerUserOpt = repository.findById(customerShipmentMap.getCustomerId());
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

    private Shipment createShipmentModelEntity(ShipmentRequestDto shipmentRequestDto) {
        Shipment shipment = new Shipment();
        this.populateCreateShipmentModelEntity(shipmentRequestDto, shipment);
        return shipment;
    }
    private Shipment updateShipmentModelEntity(UpdateShipmentRequestDto shipmentRequestDto) {
        Shipment shipment = new Shipment();
        this.populateUpdateShipmentModelEntity(shipmentRequestDto, shipment);
        return shipment;
    }
    private Shipment createShippingRateModelEntity(ShippingRateRequestDto shippingRateRequestDto) {
        Shipment shipment = new Shipment();
        this.populateCreateShippingRateModelEntity(shippingRateRequestDto, shipment);
        return shipment;
    }

    private void populateCreateShipmentModelEntity(ShipmentRequestDto shipmentRequestDto, Shipment shipment) {
        ShipmentPopulator shipmentPopulator = new ShipmentPopulator(shippingServiceRepository, repository, packageCategoryRepository, senderDetailsService, receiverDetailsService, shipmentRepository, servicePortfolioService, pickupStateRepository, pickupLocationRepository, customerShipmentMapRepository);
        shipmentPopulator.populate(shipmentRequestDto, shipment);
    }
    private void populateUpdateShipmentModelEntity(UpdateShipmentRequestDto shipmentRequestDto, Shipment shipment) {
        UpdateShipmentPopulator shipmentPopulator = new UpdateShipmentPopulator(shippingServiceRepository, repository, packageCategoryRepository, senderDetailsService, receiverDetailsService, shipmentRepository, servicePortfolioService, customerShipmentMapRepository);
        shipmentPopulator.populate(shipmentRequestDto, shipment);
    }
    private void populateCreateShippingRateModelEntity(ShippingRateRequestDto shippingRateRequestDto, Shipment shipment) {
        ShippingRatePopulator shippingRatePopulator = new ShippingRatePopulator(repository, packageCategoryRepository, senderDetailsService, receiverDetailsService, shipmentRepository, shippingServiceRepository, servicePortfolioService, customerShipmentMapRepository);
        shippingRatePopulator.populate(shippingRateRequestDto, shipment);
    }

    private Shipment saveShipment(Shipment shipment) {
        return this.shipmentRepository.save(shipment);
    }

    @Override
    public ListShipmentDto trackShipment(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber);

        if (shipment != null) {
            ListShipmentDto shipmentDTO = convertToDTO(shipment);
            return shipmentDTO;
        } else {
            // Redirect to third-party API
            throw new ShipmentNotFoundException("Shipment not found with tracking number: " + trackingNumber + " I will redirect you to our third party");
        }
    }

    @Override
    public Optional<Shipment> findByTrackingNo(final String trackingNo) {
        return Optional.ofNullable(this.shipmentRepository.findByTrackingNumber(trackingNo));
    }

    private ListShipmentDto convertToDTO(Shipment shipment) {
        ListShipmentDto dto = new ListShipmentDto();

        // Fetch SenderDetails by shipmentId
        Optional<SenderDetails> senderDetailsOpt = senderDetailsService.findByShipmentRef(shipment.getShipmentRef());
        if (senderDetailsOpt.isPresent()) {
            SenderDetails senderDetails = senderDetailsOpt.get();
            SenderDetailsDto senderDetailsDto = new SenderDetailsDto();
            senderDetailsDto.setFirstName(senderDetails.getFirstName());
            senderDetailsDto.setLastName(senderDetails.getLastName());
            senderDetailsDto.setCompanyName(senderDetails.getCompanyName());
            senderDetailsDto.setCountry(senderDetails.getCountry());
            senderDetailsDto.setAddress(senderDetails.getAddress());
            senderDetailsDto.setState(senderDetails.getState());
            senderDetailsDto.setCity(senderDetails.getCity());
            senderDetailsDto.setZipcode(senderDetails.getZipcode());
            senderDetailsDto.setEmail(senderDetails.getEmail());
            senderDetailsDto.setPhoneNo(senderDetails.getPhoneNo());
            dto.setSenderDetails(senderDetailsDto);
        }

        // Fetch ReceiverDetails by shipmentId
        Optional<ReceiverDetails> receiverDetailsOpt = receiverDetailsService.findByShipmentRef(shipment.getShipmentRef());
        if (receiverDetailsOpt.isPresent()) {
            ReceiverDetails receiverDetails = receiverDetailsOpt.get();
            ReceiverDetailsDto receiverDetailsDto = new ReceiverDetailsDto();
            receiverDetailsDto.setFirstName(receiverDetails.getFirstName());
            receiverDetailsDto.setLastName(receiverDetails.getLastName());
            receiverDetailsDto.setCompanyName(receiverDetails.getCompanyName());
            receiverDetailsDto.setCountry(receiverDetails.getCountry());
            receiverDetailsDto.setAddress(receiverDetails.getAddress());
            receiverDetailsDto.setState(receiverDetails.getState());
            receiverDetailsDto.setCity(receiverDetails.getCity());
            receiverDetailsDto.setZipcode(receiverDetails.getZipcode());
            receiverDetailsDto.setEmail(receiverDetails.getEmail());
            receiverDetailsDto.setPhoneNo(receiverDetails.getPhoneNo());
            dto.setReceiverDetails(receiverDetailsDto);
        }


        dto.setTrackingNumber(shipment.getTrackingNumber());
        dto.setTotalNumberOfPackages(shipment.getTotalNumberOfPackages());
        dto.setTotalShipmentValue(shipment.getTotalShipmentValue());
        dto.setShipmentStatus(String.valueOf(shipment.getStatus()));
        dto.setPaymentStatus(String.valueOf(shipment.getPaymentStatus()));
        dto.setVolumetricWeight(shipment.getVolumetricWeight());
        dto.setServicePortfolio(String.valueOf(shipment.getShippingService()));
        dto.setTotalShipmentWeight(shipment.getTotalShipmentWeight());
        return dto;
    }


    private void storeFiles(Shipment shipment, MultipartFile paymentProof) {

        String filename, fileDir, fileNamePath;
        //store uploaded files
        //paymentProof
        if (FileUploadValidatorUtils.isFileUploaded(paymentProof)) {
            try {
                //build file path
                filename = FilePathUtils.buildUniqueFileName(paymentProof);
                fileDir = FilePathUtils.buildCustomerUserProfilePicUploadPath();
                fileNamePath = fileDir + File.separator + filename;

                //if edit mode delete existing file
                if (shipment.getPaymentProof() != null) {
                    this.fileStorageService.deleteFile(fileDir + File.separator + shipment.getPaymentProof());
                }
                //store file
                this.fileStorageService.storeFile(paymentProof, fileNamePath);
                //add or update record
                shipment.setPaymentProof(filename);

            } catch (Exception e) {
                log.error("Unable to store uploaded paymentProof", e);
            }
        }
    }

    @Override
    @Transactional
    public List<InternationalShipmentResponseDto> getShipmentsWithNullCustomerId() {
        // Get all shipment references associated with customer IDs
        List<String> shipmentRefsWithCustomer = customerShipmentMapRepository.findAllShipmentRefs();

        // Get all shipment references in the shipment table
        List<String> allShipmentRefs = shipmentRepository.findAllShipmentRefs();

        // Determine shipment refs that do not have an associated customer
        List<String> shipmentRefsWithoutCustomer = allShipmentRefs.stream()
                .filter(ref -> !shipmentRefsWithCustomer.contains(ref))
                .collect(Collectors.toList());

        // Fetch shipments using the shipment references without a customer
        List<Shipment> shipments = shipmentRepository.findAllByShipmentRefIn(shipmentRefsWithoutCustomer);

        // Map shipments to DTOs
        return shipments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


}
