package com.tvdgapp.services.rider;

import com.tvdgapp.dtos.rider.*;
import com.tvdgapp.dtos.shipment.ShipmentDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.models.user.rider.RiderUser;
import com.tvdgapp.services.generic.TvdgEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RiderUserService extends TvdgEntityService<Long, RiderUser> {
    RiderUser createRiderUser(RiderUserDto requestDto);
    RiderUserDetailResponseDto fetchRiderUserDetail(Long userId);
//    ShipmentDto assignShipmentToRider(Long riderId, Long shipmentId);

    RiderUser updateRiderUser(final long userId, UpdateRiderUserDetailDto riderUserDetailDto);

    Optional<String> updateProfilePic(long userId, MultipartFile profilePic);
    void changePassword(String email, ChangePasswordDto changePasswordDto);

    public Optional<RiderUser> deleteRiderUserById(Long id);

    Page<ListRiderUserDto> listUsers(Pageable pageable);

    List<RiderUserResponseDto> getAllRiderUsers();
    Optional<RiderUserResponseDto> getRiderUserById(Long id);

}
