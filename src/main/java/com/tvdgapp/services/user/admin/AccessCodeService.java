package com.tvdgapp.services.user.admin;

import com.tvdgapp.dtos.user.admin.AccessCodeDTO;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.AccessCodeMapper;
import com.tvdgapp.models.user.admin.AccessCode;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.repositories.User.admin.AccessCodeRepository;
import com.tvdgapp.repositories.User.admin.AdminUserRepository;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.utils.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccessCodeService {

    private final AdminUserRepository adminRepository;

    private final AccessCodeRepository accessCodeRepository;

    private final AccessCodeMapper accessCodeMapper;
    private final UserInfoUtil userInfoUtil;

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int ACCESS_CODE_LENGTH = 6; // Set to 6 characters

    private final Random random = new SecureRandom();

    @Transactional
    public void createAccessCode(Long userId) {
        AdminUser admin = adminRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + userId));

        SecuredUserInfo user = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();

        AccessCode accessCode = new AccessCode();
        accessCode.setUserId(admin.getId());
        accessCode.setAccessCode(generateUniqueAccessCode());
        accessCode.setCreatedBy(user.getUserId());
        accessCode.setCreatedAt(LocalDateTime.now());
        accessCode.setTsCreated((int) (System.currentTimeMillis() / 1000));

        AccessCode savedAccessCode = accessCodeRepository.save(accessCode);
        accessCodeMapper.toDto(savedAccessCode);
    }

    @Transactional
    public AccessCodeDTO updateAccessCode(Long id) {
        AccessCode accessCode = accessCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Access code not found with ID: " + id));
        SecuredUserInfo user = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();

        accessCode.setAccessCode(generateUniqueAccessCode());
        accessCode.setUpdatedBy(user.getUserId());
        accessCode.setUpdatedAt(LocalDateTime.now());
        accessCode.setTsModified((int) (System.currentTimeMillis() / 1000));

        AccessCode updatedAccessCode = accessCodeRepository.save(accessCode);
        return accessCodeMapper.toDto(updatedAccessCode);
    }

    @Transactional
    public void deleteAccessCode(Long id) {
        AccessCode accessCode = accessCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Access code not found with ID: " + id));
        accessCodeRepository.delete(accessCode);
    }

    private String generateUniqueAccessCode() {
        StringBuilder accessCode = new StringBuilder(ACCESS_CODE_LENGTH);
        for (int i = 0; i < ACCESS_CODE_LENGTH; i++) {
            accessCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return accessCode.toString();
    }
}
