package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.ShippingProfileDto;
import com.tvdgapp.dtos.shipment.ShippingProfileRequestDto;
import com.tvdgapp.exceptions.AuthenticationException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.shipment.ShippingProfile;
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
public class ShippingProfileServiceImpl extends TvdgEntityServiceImpl<Long, ShippingProfile> implements ShippingProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingProfileServiceImpl.class);



    private final ShippingProfileRepository shippingProfileRepository;
    private final CustomerUserRepository customerUserRepository;

    @Override
    @Transactional
    public ShippingProfile createShippingProfile(ShippingProfileRequestDto profileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Optional<CustomerUser> customerUserOptional = customerUserRepository.findByEmail(userDetails.getUsername());
            if (customerUserOptional.isPresent()) {
                CustomerUser customerUser = customerUserOptional.get();
                if (isDuplicateProfile(profileDto, customerUser.getId())) {
                    throw new IllegalArgumentException("A shipping profile with similar details already exists.");
                }

                ShippingProfile shippingProfile = new ShippingProfile();
                // Map fields from DTO to entity
                shippingProfile.setShipmentName(profileDto.getShipmentName());
                shippingProfile.setShippingMode(profileDto.getShippingMode());
                shippingProfile.setPickupDays(profileDto.getPickupDays());
                shippingProfile.setPickupLocation(profileDto.getPickupLocation());
                shippingProfile.setAverageDailyPackages(profileDto.getAverageDailyPackages());
                shippingProfile.setAverageDailyWeight(profileDto.getAverageDailyWeight());
                shippingProfile.setNumberOfMonthlyPackages(profileDto.getNumberOfPackagesPerMonth());
                shippingProfile.setSubscription(profileDto.getSubscription());
                shippingProfile.setMonthlyRevenue(profileDto.getMonthlyRevenue());
                shippingProfile.setServiceInterests(profileDto.getServiceInterests());
                shippingProfile.setCustomerUser(customerUser);

                logger.info("Creating shipping profile for user: {}", customerUser.getEmail());
                return shippingProfileRepository.save(shippingProfile);
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
        Optional<ShippingProfile> existingProfile = shippingProfileRepository.findByShipmentNameAndShippingModeAndCustomerUserId(
                requestDto.getShipmentName(), requestDto.getShippingMode(), customerId);

        return existingProfile.isPresent();
    }



    public List<ShippingProfile> listShippingProfiles () {
        return shippingProfileRepository.findAll();
    }
    @Override
    @Transactional
    public List<ShippingProfileDto> getAllShippingProfiles() {
        List<ShippingProfile> shippingProfiles = shippingProfileRepository.findAll();

        return shippingProfiles.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public List<ShippingProfile> getShippingProfilesByCustomerUserId(Long customerUserId) {
        List<ShippingProfile> shippingProfiles = shippingProfileRepository.findByCustomerUser_Id(customerUserId);

        // Initialize lazy-loaded collections or attributes as needed
        // Example of initializing a lazy-loaded collection
        shippingProfiles.forEach(ShippingProfile::getServiceInterests);

        return shippingProfiles;
    }

    private ShippingProfileDto mapEntityToDto(ShippingProfile shippingProfile) {
        ShippingProfileDto dto = new ShippingProfileDto();
        dto.setId(shippingProfile.getId());
        dto.setShipmentName(shippingProfile.getShipmentName());
        dto.setShippingMode(shippingProfile.getShippingMode());
        dto.setPickupLocation(shippingProfile.getPickupLocation());
        dto.setPickupDays(shippingProfile.getPickupDays());
        dto.setAverageDailyPackages(shippingProfile.getAverageDailyPackages());
        dto.setAverageDailyWeight(shippingProfile.getAverageDailyWeight());
        dto.setNumberOfPackagesPerMonth(shippingProfile.getNumberOfMonthlyPackages());
        dto.setSubscription(shippingProfile.getSubscription());
        dto.setServiceInterests(shippingProfile.getServiceInterests());
        dto.setMonthlyRevenue(shippingProfile.getMonthlyRevenue());
        return dto;
    }

}

