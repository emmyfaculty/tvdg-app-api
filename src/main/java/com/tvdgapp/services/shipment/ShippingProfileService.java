package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.ShippingProfileDto;
import com.tvdgapp.dtos.shipment.ShippingProfileRequestDto;
import com.tvdgapp.models.shipment.ShippingProfile;
import com.tvdgapp.services.generic.TvdgEntityService;

import java.util.List;

public interface ShippingProfileService extends TvdgEntityService<Long, ShippingProfile> {

    public ShippingProfile createShippingProfile(ShippingProfileRequestDto profileDto);

    List<ShippingProfileDto> getAllShippingProfiles() ;
    List<ShippingProfile> getShippingProfilesByCustomerUserId(Long customerUserId);

}
