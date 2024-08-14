package com.tvdgapp.populator;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.affiliate.AffiliateUserDto;
import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.UserType;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.utils.CodeGeneratorUtils;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@SuppressWarnings("NullAway.Init")
public class AffiliateUserPopulator extends AbstractDataPopulator<AffiliateUserDto, AffiliateUser> {


    private PasswordEncoder passwordEncoder;

    @Override
    public AffiliateUser populate(AffiliateUserDto source, AffiliateUser target) {

        target.setTitle(source.getTitle());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setTelephoneNumber(source.getPhone());
        target.setUsername(source.getUsername());
        target.setStatus(UserStatus.PENDING);
        target.setUserType(UserType.AFFILIATE);
        target.setPassword(source.getPassword());
        target.setPostalCode(source.getPostalCode());
        target.setIdentificationNumber(source.getIdentificationNumber());
        target.setIdentificationType(source.getIdentificationType());
        target.setGender(source.getGender());
        target.setReferralCode(CodeGeneratorUtils.generateReferralCode());
        target.setCountry(source.getCountry());
        target.setState(source.getState());
        target.setStreetAddress(source.getStreetAddress());
        target.setCity(source.getCity());
        if (StringUtils.isNotEmpty(source.getDateOfBirth())) {
            target.setDateOfBirth(TvdgAppDateUtils.formatStringToDate(source.getDateOfBirth(), DateConstants.DATE_INPUT_FORMAT));
        }
        return target;
    }


    @Override
    protected @Nullable AffiliateUser createTarget() {
        return null;
    }
}
