package com.tvdgapp.services.affiliate;

import com.tvdgapp.dtos.affiliate.*;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.shipment.ShipmentDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.services.generic.TvdgEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AffiliateUserService extends TvdgEntityService<Long, AffiliateUser> {
    AffiliateUser createAffiliateUser(AffiliateUserDto requestDto);

//    AffiliateUserDetailDto fetchAffiliateUserDetail(Long customerUserId);
//    AffiliateUserDetailDto fetchAffiliateUserDetail(final Long userId);
AffiliateUserDetailResponseDto fetchAffiliateDetail(Long userId);

    AffiliateUser updateAffiliateUser(final long customerUserId, UpdateAffiliateUserDetailDto affiliateUserDetailDto);

    Optional<String> updateProfilePic(long userId, MultipartFile profilePic);
    void changePassword(String email, ChangePasswordDto changePasswordDto);
    public void processReferral(Long shipmentId);
    void publishForgotPasswordEmail(String email);
    void resetPassword(ResetPasswordDto resetPasswordDto);
    public void deleteAffiliateUser(Long id);

    Page<ListAffiliateUserDto> listUsers(Pageable pageable);

    AffiliateUser approveAffiliateUser(Long id);
    AffiliateUser deactivateAffiliateUser(Long userId);

    Optional<AffiliateUserDtoResponse> getAffiliateUserById(Long id);
    List<AffiliateUserDtoResponse> getAllAffiliateUsers();
    List<AffiliateResponseDto> listAllAffiliatesByStatus(String status);
    long getTotalAffiliateCountByStatus(String status);
    AffiliateResponseDto getAffiliateByReferralCode(String referralCode);
    AffiliateResponseDto updateAffiliate(Long id, AffiliateUserDto affiliateUserDTO);
    List<ShipmentDto> listAllShipmentsByReferralCode(String referralCode);
    AffiliateUser approveAffiliateRequest(Long id);
    AffiliateUser rejectAffiliateRequest(Long id);
    BigDecimal getTotalEarningsByReferralCode(String referralCode);
    BigDecimal getTotalSalesAmountByReferralCode(String referralCode);

}
