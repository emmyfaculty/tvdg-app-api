package com.tvdgapp.mapper;

import com.tvdgapp.dtos.affiliate.AffiliateResponseDto;
import com.tvdgapp.dtos.affiliate.AffiliateUserDto;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AffiliateUserMapper {
    AffiliateResponseDto toAffiliateUserDTO(AffiliateUser affiliateUser);
    AffiliateUser toAffiliateUser(AffiliateUserDto affiliateUserDTO);
}


