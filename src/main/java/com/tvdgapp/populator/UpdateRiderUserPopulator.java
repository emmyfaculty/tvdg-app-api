package com.tvdgapp.populator;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.rider.UpdateRiderUserDetailDto;
import com.tvdgapp.models.user.rider.RiderUser;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@SuppressWarnings("NullAway.Init")
public class UpdateRiderUserPopulator extends AbstractDataPopulator<UpdateRiderUserDetailDto, RiderUser> {


    private PasswordEncoder passwordEncoder;

    @Override
    public RiderUser populate(UpdateRiderUserDetailDto source, RiderUser target) {

        target.setTitle(source.getTittle());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        if (StringUtils.isNotEmpty(source.getDateOfBirth())) {
            target.setDateOfBirth(TvdgAppDateUtils.formatStringToDate(source.getDateOfBirth(), DateConstants.DATE_INPUT_FORMAT));
        }
        target.setEmployeeId(source.getEmployeeId());
        return target;
    }


    @Override
    protected @Nullable RiderUser createTarget() {
        return null;
    }
}
