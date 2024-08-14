package com.tvdgapp.services.user;

import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.user.*;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.services.generic.TvdgEntityService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerUserService extends TvdgEntityService<Long, CustomerUser> {
    CustomerUser createCustomerUser(CustomerUserDto requestDto);

    CustomerUserDetailDto fetchCustomerUserDetail(Long customerUserId);

    CustomerUser updateCustomerUser(final long customerUserId, CustomerUserDto customerUserDto, MultipartFile profileFileUpload);

    void changePassword(String email, ChangePasswordDto changePasswordDto);

    Optional<String> updateProfilePic(long userId, MultipartFile profilePic);
    void publishForgotPasswordEmail(String email);
    void resetPassword(ResetPasswordDto resetPasswordDto);

    List<CustomerUserDtoResponse> getAllCustomerUsers();
    CustomerUserDtoResponse getCustomerUserById(Long id);
    CustomerUser updateCustomerUser(Long userId, UpdateCustomerUserDto updateCustomerUserDto);
    CustomerUser updateCustomerUser(Long userId, UpdateCustomerAccountInfoDto updateDto);

}
