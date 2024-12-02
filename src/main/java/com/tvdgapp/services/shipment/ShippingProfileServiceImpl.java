package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.ShippingProfileDto;
import com.tvdgapp.dtos.shipment.ShippingProfileRequestDto;
import com.tvdgapp.exceptions.AuthenticationException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.shipment.CustomerShippingProfile;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.shipment.ShippingProfileRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingProfileServiceImpl extends TvdgEntityServiceImpl<Long, CustomerShippingProfile> implements ShippingProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingProfileServiceImpl.class);



    private final ShippingProfileRepository shippingProfileRepository;
    private final CustomerUserRepository customerUserRepository;

    @Override
    @Transactional
    public CustomerShippingProfile createShippingProfile(ShippingProfileRequestDto profileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Optional<CustomerUser> customerUserOptional = customerUserRepository.findByEmail(userDetails.getUsername());
            if (customerUserOptional.isPresent()) {
                CustomerUser customerUser = customerUserOptional.get();
                if (isDuplicateProfile(profileDto, customerUser.getId())) {
                    throw new IllegalArgumentException("A shipping profile with similar details already exists.");
                }

                CustomerShippingProfile customerShippingProfile = new CustomerShippingProfile();
                // Map fields from DTO to entity
                customerShippingProfile.setShipmentName(profileDto.getShipmentName());
                customerShippingProfile.setShippingMode(profileDto.getShippingMode());
                customerShippingProfile.setPickupDays(profileDto.getPickupDays());
                customerShippingProfile.setPickupLocation(profileDto.getPickupLocation());
                customerShippingProfile.setAverageDailyPackages(profileDto.getAverageDailyPackages());
                customerShippingProfile.setAverageDailyWeight(profileDto.getAverageDailyWeight());
                customerShippingProfile.setNumberOfMonthlyPackages(profileDto.getNumberOfPackagesPerMonth());
                customerShippingProfile.setSubscription(profileDto.getSubscription());
                customerShippingProfile.setMonthlyRevenue(profileDto.getMonthlyRevenue());
                customerShippingProfile.setServiceInterests(profileDto.getServiceInterests());
                customerShippingProfile.setCustomerUser(customerUser);

                logger.info("Creating shipping profile for user: {}", customerUser.getEmail());
                return shippingProfileRepository.save(customerShippingProfile);
            } else {
                logger.error("Customer user not found");
                throw new ResourceNotFoundException("Customer user not found");
            }
        } else {
            logger.error("User not authenticated");
            throw new AuthenticationException("User not authenticated");
        }
    }

    private boolean isDuplicateProfile(ShippingProfileRequestDto requestDto, Long customerId) {
        // Query the repository to check if a similar profile exists for the customer
        // Example: check by name and shipping mode
        Optional<CustomerShippingProfile> existingProfile = shippingProfileRepository.findByShipmentNameAndShippingModeAndCustomerUserId(
                requestDto.getShipmentName(), requestDto.getShippingMode(), customerId);

        return existingProfile.isPresent();
    }



    public List<CustomerShippingProfile> listShippingProfiles () {
        return shippingProfileRepository.findAll();
    }
    @Override
    @Transactional
    public List<ShippingProfileDto> getAllShippingProfiles() {
        List<CustomerShippingProfile> customerShippingProfiles = shippingProfileRepository.findAll();

        return customerShippingProfiles.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public List<CustomerShippingProfile> getShippingProfilesByCustomerUserId(Long customerUserId) {
        List<CustomerShippingProfile> customerShippingProfiles = shippingProfileRepository.findByCustomerUser_Id(customerUserId);

        // Initialize lazy-loaded collections or attributes as needed
        // Example of initializing a lazy-loaded collection
        customerShippingProfiles.forEach(CustomerShippingProfile::getServiceInterests);

        return customerShippingProfiles;
    }

    private ShippingProfileDto mapEntityToDto(CustomerShippingProfile customerShippingProfile) {
        ShippingProfileDto dto = new ShippingProfileDto();
        dto.setId(customerShippingProfile.getId());
        dto.setShipmentName(customerShippingProfile.getShipmentName());
        dto.setShippingMode(customerShippingProfile.getShippingMode());
        dto.setPickupLocation(customerShippingProfile.getPickupLocation());
        dto.setPickupDays(customerShippingProfile.getPickupDays());
        dto.setAverageDailyPackages(customerShippingProfile.getAverageDailyPackages());
        dto.setAverageDailyWeight(customerShippingProfile.getAverageDailyWeight());
        dto.setNumberOfPackagesPerMonth(customerShippingProfile.getNumberOfMonthlyPackages());
        dto.setSubscription(customerShippingProfile.getSubscription());
        dto.setServiceInterests(customerShippingProfile.getServiceInterests());
        dto.setMonthlyRevenue(customerShippingProfile.getMonthlyRevenue());
        return dto;
    }

}

