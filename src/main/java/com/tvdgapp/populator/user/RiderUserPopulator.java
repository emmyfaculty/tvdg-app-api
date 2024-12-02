package com.tvdgapp.populator.user;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.rider.RiderUserDto;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.UserType;
import com.tvdgapp.models.user.rider.RiderUser;
import com.tvdgapp.populator.AbstractDataPopulator;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@SuppressWarnings("NullAway.Init")
public class RiderUserPopulator extends AbstractDataPopulator<RiderUserDto, RiderUser> {


    private PasswordEncoder passwordEncoder;

    @Override
    public RiderUser populate(RiderUserDto source, RiderUser target) {

        target.setTitle(source.getTitle());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhoneCode(source.getPhoneCode());
        target.setPhone(source.getPhone());
        target.setStatus(UserStatus.ACTIVE);
        target.setUserType(UserType.RIDER);
        if (StringUtils.isNotEmpty(source.getDateOfBirth())) {
            target.setDateOfBirth(TvdgAppDateUtils.formatStringToDate(source.getDateOfBirth(), DateConstants.DATE_INPUT_FORMAT));
        }
        target.setEnableOtp(true);
        target.setEmployeeId(source.getEmployeeId());
        return target;
    }


    @Override
    protected @Nullable RiderUser createTarget() {
        return null;
    }
}
