package com.tvdgapp.populator;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.user.CustomerUserDto;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.UserType;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@SuppressWarnings("NullAway.Init")
public class CustomerUserPopulator extends AbstractDataPopulator<CustomerUserDto, CustomerUser> {

    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerUser populate(CustomerUserDto source, CustomerUser target) {
        target.setTitle(source.getTitle());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhoneCode(source.getPhoneCode());
        target.setPhone(source.getPhone());
        target.setCompanyName(source.getCompanyName());
        target.setCompanyContactName(source.getCompanyContactName());
        target.setCompanyEmail(source.getCompanyEmail());
        target.setCompanyPhoneNo(source.getCompanyPhoneNo());
        target.setCompanyRegNumber(source.getCompanyRegNumber());
        target.setPostalCode(source.getPostalCode());
        target.setIndustry(source.getIndustry());
        target.setUserType(UserType.CUSTOMER);
        target.setCustomerType(source.getCustomerType());

        // Validate and set the PricingCategory
        if (source.getPricingLevelId() != null) {
            try {
                target.setPricingLevelId(source.getPricingLevelId());
            } catch (IllegalArgumentException e) {
                // Handle the error or set a default value
                throw new IllegalArgumentException("Invalid PricingCategory value: " + source.getPricingLevelId());
            }
        }

        target.setState(source.getState());
        target.setDesignation(source.getDesignation());
        target.setAddress(source.getAddress());
        target.setCity(source.getCity());

        if (StringUtils.isNotEmpty(source.getDateOfBirth())) {
            target.setDateOfBirth(TvdgAppDateUtils.formatStringToDate(
                    source.getDateOfBirth(), DateConstants.DATE_INPUT_FORMAT));
        }

        target.setNatureOfBusiness(source.getNatureOfBusiness());

        target.setStatus(UserStatus.valueOf(source.getStatus()));
        target.setEnableOtp(false);
        return target;
    }

        @Override
    protected @Nullable CustomerUser createTarget() {
        return null;
    }
}
