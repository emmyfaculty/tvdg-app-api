package com.tvdgapp.dtos.affiliate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class AffiliateUserDetailResponseDto {
    @JsonProperty(value = "user")
    private AffiliateUserDetailDto affiliateUserDto;
    private Map<String,Object> extras;

    public AffiliateUserDetailDto getAffiliateUserDto() {
        if (affiliateUserDto == null) {
            affiliateUserDto = new AffiliateUserDetailDto();
        }
        return affiliateUserDto;
    }
}