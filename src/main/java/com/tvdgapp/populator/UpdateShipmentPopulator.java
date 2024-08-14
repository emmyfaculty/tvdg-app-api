package com.tvdgapp.populator;

import com.tvdgapp.dtos.shipment.*;
import com.tvdgapp.exceptions.AuthenticationException;
import com.tvdgapp.exceptions.TvdgException;
import com.tvdgapp.models.shipment.*;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.shipment.CustomerShipmentMapRepository;
import com.tvdgapp.repositories.shipment.PackageCategoryRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import com.tvdgapp.services.reference.states.StatesService;
import com.tvdgapp.services.shipment.ReceiverDetailsService;
import com.tvdgapp.services.shipment.SenderDetailsService;
import com.tvdgapp.services.shipment.pricingcaculation.ServicePortfolioService;
import com.tvdgapp.utils.CodeGeneratorUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Getter
@Setter
@SuppressWarnings("NullAway.Init")
public class UpdateShipmentPopulator extends AbstractDataPopulator<UpdateShipmentRequestDto, Shipment>  {

//    private final ShippingOptionRepository shippingOptionRepository;
    private CustomerUserRepository repository;
    private  PackageCategoryRepository packageCategoryRepository;
    private StatesService statesService;
    private final SenderDetailsService senderDetailsService;
    private final ReceiverDetailsService receiverDetailsService;
    private final ShipmentRepository shipmentRepository;
    private final ShippingServiceRepository shippingServiceRepository;
    private final ServicePortfolioService servicePortfolioService;
    private final CustomerShipmentMapRepository customerShipmentMapRepository;


    public UpdateShipmentPopulator(ShippingServiceRepository shippingServiceRepository, CustomerUserRepository repository, PackageCategoryRepository packageCategoryRepository, SenderDetailsService senderDetailsService, ReceiverDetailsService receiverDetailsService,
                                   ShipmentRepository shipmentRepository, ServicePortfolioService servicePortfolioService, CustomerShipmentMapRepository customerShipmentMapRepository) {
        this.shippingServiceRepository = shippingServiceRepository;
        this.repository = repository;

        this.packageCategoryRepository = packageCategoryRepository;
        this.senderDetailsService = senderDetailsService;
        this.receiverDetailsService = receiverDetailsService;
        this.shipmentRepository = shipmentRepository;
        this.servicePortfolioService = servicePortfolioService;
        this.customerShipmentMapRepository = customerShipmentMapRepository;
//        this.priceModelLevelRepository = priceModelLevelRepository;
//        this.pricingModelRepository = pricingModelRepository;
    }

    @Override
    public Shipment populate(UpdateShipmentRequestDto source, Shipment target) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
            // Retrieve the authenticated user's details
            Optional<CustomerUser> customerUser = repository.findByEmail(userDetails.getUsername());
            if (customerUser.isPresent()) {
                CustomerUser customer = customerUser.get();

                // Create a new entry in the CustomerShipmentMap
                CustomerShipmentMap customerShipmentMap = new CustomerShipmentMap();
                customerShipmentMap.setCustomerId(customer.getId());
                customerShipmentMap.setShipmentRef(target.getShipmentRef());

                // Save the CustomerShipmentMap entry
                customerShipmentMapRepository.save(customerShipmentMap);

                // Set the PricingLevelId from CustomerUser
                target.setPricingLevelId(customer.getPricingLevelId());
            } else {
                throw new AuthenticationException("Authentication failed for authenticated customer");
            }
        } else {
            // Handle guest users (not authenticated)
            target.setPricingLevelId(null); // Set a default or guest PricingLevelId if necessary
        }
        // Set other shipment details
//        target.setCustomerType(source.getCustomerType());
//        target.setShipmentType(source.getShipmentType());
//        target.setServiceType(source.getServiceType());
        target.setTotalNumberOfPackages(source.getTotalNumberOfPackages());
        target.setTotalShipmentValue(source.getTotalShipmentValue());
        target.setDimension(source.getDimension());
        target.setTotalShipmentWeight(source.getTotalShipmentWeight());
        target.setUnits(source.getUnits());
        target.setReferralCode(source.getReferralCode());
        target.setTrackingNumber(CodeGeneratorUtils.generateTrackingNumber());
        target.setStatus("PENDING");
        target.setPaymentStatus(PaymentStatus.PENDING);
        target.setVerificationStatus(VerificationStatus.PENDING);

        // Calculate volumetric weight
        double volumetricWeight = (source.getDimension().getLength() *
                source.getDimension().getWidth() *
                source.getDimension().getHeight()) / 5000.0;
        target.setVolumetricWeight(volumetricWeight);
        double weightToUse = Math.max(volumetricWeight, source.getTotalShipmentWeight());

        // Calculate packaging fee
        BigDecimal packagingFee = BigDecimal.valueOf(0.0);
        if (source.isUseOurPackaging()) {
            packagingFee = BigDecimal.valueOf(source.getTotalNumberOfPackages() * 3000);
        }
        target.setPackagingFee(packagingFee);

        // Calculate total shipment amount
        Set<ProductItem> productItems = new HashSet<>();
        BigDecimal totalCategoryDocumentAmount = BigDecimal.valueOf(0.0);

        for (ProductItemDto itemDto : source.getProductItems()) {
            ProductItem productItem = new ProductItem();
            productItem.setDescription(itemDto.getDescription());
            productItem.setQuantity(itemDto.getQuantity());
            productItem.setUnits(itemDto.getUnits());
            productItem.setValue(itemDto.getValue());
            productItem.setWeight(itemDto.getWeight());
            productItem.setManufacturingCountry(itemDto.getManufacturingCountry());

            PackageCategory packageCategory = packageCategoryRepository.findByCategoryName(itemDto.getPackageCategoryName())
                    .orElseThrow(() -> new IllegalArgumentException("Package category not found"));
            productItem.setPackageCategory(packageCategory);
            totalCategoryDocumentAmount.add(packageCategory.getCategoryAmount());

            // Set the shipment reference
            productItem.setShipment(target);

            productItems.add(productItem);
        }
        target.setProductItems(productItems);

        ShippingService shippingService = shippingServiceRepository.findById(source.getServicePortfolioId())
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found"));
        target.setShippingService(shippingService);
        PricingLevel pricingLevel = new PricingLevel();
        Integer pricingCategory = target.getPricingLevelId() != null ? target.getPricingLevelId() : 1;

        String shipmentCountry = source.getReceiverDetails().getCountry();

        if (shippingService.getServiceName().toLowerCase().startsWith("import")) {
            shipmentCountry = source.getSenderDetails().getCountry();
        }

        PriceModelLevel priceModelLevel = servicePortfolioService.getPriceModelLevelForShipment(
                        Long.valueOf(shippingService.getServiceId()), BigDecimal.valueOf(weightToUse), shipmentCountry, pricingCategory, String.valueOf(source.getUnits()))
                .orElseThrow(() -> new IllegalArgumentException("No matching PricingModelLevel found"));

        // Calculate the base cost
        double convertedWeightToUse = convertWeightToKg(weightToUse, String.valueOf(source.getUnits()));
        double baseCost = calculateBaseCost(priceModelLevel, convertedWeightToUse);
        target.setTransportCharge(BigDecimal.valueOf(baseCost));

        BigDecimal vat = BigDecimal.valueOf(baseCost * 0.075);
//        BigDecimal totalShipmentAmount = baseCost + vat + packagingFee + totalCategoryDocumentAmount;
        BigDecimal totalShipmentAmount = BigDecimal.valueOf(baseCost)
                .add(vat)
                .add(packagingFee)
                .add(totalCategoryDocumentAmount);


        // Check if insurance is selected and partner is available
        if (source.isInsurance()) {
            BigDecimal insuranceFee = source.getTotalShipmentValue().multiply(BigDecimal.valueOf(0.01));
            totalShipmentAmount.add(insuranceFee);
            target.setInsuranceFee(insuranceFee);
        } else {
            target.setInsuranceFee(BigDecimal.valueOf(0));
        }

//        target.setTotalShipmentAmount(Math.round(totalShipmentAmount * 100.0) / 100.0);
        BigDecimal roundedTotalShipmentAmount = totalShipmentAmount.setScale(2, RoundingMode.HALF_UP);
        target.setTotalShipmentAmount(roundedTotalShipmentAmount);
        target.setVat(vat);
        target.setPickup(source.getPickup());
        target.setPickupFee(source.getPickupFee() != null ? source.getPickupFee() : BigDecimal.ZERO);

        // Save the shipment first to generate an ID
        target = shipmentRepository.save(target);

        // Handle sender details
        Optional<SenderDetailsDto> senderDetailsOptional = Optional.ofNullable(source.getSenderDetails());
        if (senderDetailsOptional.isPresent()) {
            SenderDetailsDto senderDetailsDto = senderDetailsOptional.get();
            SenderDetails senderDetails = new SenderDetails();
            senderDetails.setFirstName(senderDetailsDto.getFirstName());
            senderDetails.setLastName(senderDetailsDto.getLastName());
            senderDetails.setCompanyName(senderDetailsDto.getCompanyName());
            senderDetails.setCountry(senderDetailsDto.getCountry());
            senderDetails.setAddress(senderDetailsDto.getAddress());
            senderDetails.setState(senderDetailsDto.getState());
            senderDetails.setCity(senderDetailsDto.getCity());
            senderDetails.setZipcode(senderDetailsDto.getZipcode());
            senderDetails.setEmail(senderDetailsDto.getEmail());
            senderDetails.setPhoneNo(senderDetailsDto.getPhoneNo());
            senderDetails.setShipmentRef(target.getShipmentRef());
            senderDetailsService.saveOrUpdate(senderDetails);
        } else {
            throw new IllegalArgumentException("Sender details cannot be null");
        }

        // Handle receiver details
        Optional<ReceiverDetailsDto> receiverDetailsOptional = Optional.ofNullable(source.getReceiverDetails());
        if (receiverDetailsOptional.isPresent()) {
            ReceiverDetailsDto receiverDetailsDto = receiverDetailsOptional.get();
            ReceiverDetails receiverDetails = new ReceiverDetails();
            receiverDetails.setFirstName(receiverDetailsDto.getFirstName());
            receiverDetails.setLastName(receiverDetailsDto.getLastName());
            receiverDetails.setCompanyName(receiverDetailsDto.getCompanyName());
            receiverDetails.setCountry(receiverDetailsDto.getCountry());
            receiverDetails.setAddress(receiverDetailsDto.getAddress());
            receiverDetails.setState(receiverDetailsDto.getState());
            receiverDetails.setCity(receiverDetailsDto.getCity());
            receiverDetails.setZipcode(receiverDetailsDto.getZipcode());
            receiverDetails.setEmail(receiverDetailsDto.getEmail());
            receiverDetails.setPhoneNo(receiverDetailsDto.getPhoneNo());
            receiverDetails.setShipmentRef(target.getShipmentRef());
            receiverDetailsService.saveOrUpdate(receiverDetails);
        } else {
            throw new IllegalArgumentException("Receiver details cannot be null");
        }

        return target;
    }

    private double convertWeightToKg(double weight, String units) {
        if ("lb".equalsIgnoreCase(units)) {
            return weight * 0.453592;
        } else if ("oz".equalsIgnoreCase(units)) {
            return weight * 0.0283495;
        }
        return weight;
    }

    private double calculateBaseCost(PriceModelLevel priceModelLevel, double weightToUse) {
        if (priceModelLevel.getWeightBandEnd() == null) {
            return priceModelLevel.getPrice() * weightToUse;
        } else {
            return priceModelLevel.getPrice();
        }
    }


    @Override
    protected @Nullable Shipment createTarget() {
        return null;
    }
}
