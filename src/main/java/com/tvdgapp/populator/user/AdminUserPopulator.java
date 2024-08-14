package com.tvdgapp.populator.user;

import com.tvdgapp.dtos.user.UserDto;
import com.tvdgapp.dtos.user.admin.AdminUserDto;
import com.tvdgapp.dtos.user.admin.AdminUserRequestDto;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.UserType;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.populator.AbstractDataPopulator;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminUserPopulator extends
        AbstractDataPopulator<AdminUserRequestDto, AdminUser> {

    @Override
    public AdminUser populate(AdminUserRequestDto source, AdminUser target) {

        target.setEmail(source.getEmail());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setTelephoneNumber(source.getPhone());
        target.setUserType(UserType.ADMIN);
//        target.setGenerateCode(source.getGenerateCode());
        target.setStatus(UserStatus.valueOf(source.getStatus()));
        return target;
    }

    @Override
    protected @Nullable AdminUser createTarget() {
        return null;
    }


}