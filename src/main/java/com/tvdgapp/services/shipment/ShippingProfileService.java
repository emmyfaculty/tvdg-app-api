package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.ShippingProfileDto;
import com.tvdgapp.dtos.shipment.ShippingProfileRequestDto;
import com.tvdgapp.models.shipment.CustomerShippingProfile;
import com.tvdgapp.services.generic.TvdgEntityService;

import java.util.List;

public interface ShippingProfileService extends TvdgEntityService<Long, CustomerShippingProfile> {

    public CustomerShippingProfile createShippingProfile(ShippingProfileRequestDto profileDto);

    List<ShippingProfileDto> getAllShippingProfiles() ;
    List<CustomerShippingProfile> getShippingProfilesByCustomerUserId(Long customerUserId);

}
