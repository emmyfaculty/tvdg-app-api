package com.tvdgapp.services.user.admin;


import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.dtos.user.UpdateUserProfileDto;
import com.tvdgapp.dtos.user.UserProfileDto;
import com.tvdgapp.dtos.user.admin.AdminUserDetailDto;
import com.tvdgapp.dtos.user.admin.AdminUserDetailResponseDto;
import com.tvdgapp.dtos.user.admin.AdminUserRequestDto;
import com.tvdgapp.dtos.user.admin.ListAdminUserDto;
import com.tvdgapp.models.user.admin.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

public interface AdminUserService {

    AdminUser createUser(AdminUserRequestDto adminUserRequestDto);

    Page<ListAdminUserDto> listUsers(Pageable pageable);

    UserProfileDto fetchAdminUserProfile(String username);

    void changePassword(String user, ChangePasswordDto changePasswordDto);

    AdminUserDetailResponseDto fetchAdminUserDetail(final Long userId);

    AdminUser updateAdminUser(Long userId, AdminUserRequestDto dto);

    void deactivateUser(final Long userId);

    void activateUser(final Long userId);

    void create(AdminUser adminUser);

    void publishForgotPasswordEmail(String email);

    Optional<AdminUser> findUserByEmail(String email);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    AdminUserDetailDto updateProfile(long userId, UpdateUserProfileDto dto);

    Optional<String> updateProfilePic(long userId, MultipartFile profilePic);

//    UserTrackingDto getAdminUserTrackingData(Collection<SearchCriteria> searchCriteria);

    void deleteUser(Long userId);

    Optional<AdminUser> findById(long adminUserId);

    Collection<AdminUser> findUsersByIds(Collection<Long> userIds);

    AdminUser createNewUser(AdminUser user);
}
