package com.tvdgapp.services.user;

import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.exceptions.EntityType;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.user.User;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.utils.EmailTemplateUtils;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_EXPIRED;
import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_NOT_EXIST;

@Service("userServiceImpl")
@RequiredArgsConstructor
public class UserServiceImple extends TvdgEntityServiceImpl<Long, User> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImple.class);


    private final UserRepository userRepository;
    private final SystemConfigurationService systemConfigurationService;
    private final EmailTemplateUtils emailTemplateUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${api.url-domain}${api.basepath-api}")
    private @Nullable String urlDomain;
    @Override
    @Transactional
    public void publishForgotPasswordEmail(String email, String url) {

        Optional<User> user = userRepository.findAuthUserByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(EntityType.USER,email);
        }

        final String token = UUID.randomUUID().toString();
        this.createPasswordResetTokenForUser(user.get(),token);

        String passwordResetUrl = this.createPasswordResetUrl(token, url);

        this.publishPasswordResetEmail(user.get(),passwordResetUrl);

    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto resetPasswordDto) {

        this.validatePasswordResetToken(resetPasswordDto.getToken()).ifPresent((it)->{
            throw new InvalidRequestException(it);
        });

        Optional<User> user = this.findUserByPasswordResetToken(resetPasswordDto.getToken());
        if (user.isEmpty()) {
            throw new InvalidRequestException(RESET_TOKEN_NOT_EXIST);
        }
        this.resetPassword(user.get(),resetPasswordDto.getNewPassword());
        User user1 = user.get();
        user1.setPasswordResetToken(null);
//        if (otp.equals(user.getOtp())) {
//            user.setOtp(null);}
    }

    @Override
    public List<String> getUserTokensByRole(String role) {
        return userRepository.findDeviceTokensByRole(role);
    }

    private Optional<String> validatePasswordResetToken(String token) {
        if (token == null) {
            return Optional.of("Invalid token");
        }
        Optional<User> user = this.findUserByPasswordResetToken(token);
        if (user.isEmpty()) {
            return Optional.of(RESET_TOKEN_NOT_EXIST);
        }
        if (isExpiredToken(user.get())) {
            return Optional.of(RESET_TOKEN_EXPIRED);
        }
        return Optional.empty();
    }

    private boolean isExpiredToken(User user) {
        if (user.getPasswordResetVldtyTerm()!=null&&user.getPasswordResetVldtyTerm().before(TvdgAppDateUtils.now())) {
            return true;
        }
        return false;
    }

    private Optional<User> findUserByPasswordResetToken(String token) {
        return this.userRepository.findUserByPasswordResetToken(token);
    }

    private void resetPassword(User user, String password) {
        this.changePassword(user, password);
    }

    private void changePassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
    }


    private void createPasswordResetTokenForUser(User user, String token) {
        user.setPasswordResetToken(token);
        String valdtyTrm = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.PSSWORD_SETTING_TOKEN_VLDTY_TRM);
        user.calculateTokenExpiryDate(valdtyTrm);
        this.userRepository.save(user);
    }

    private String createPasswordResetUrl(String token, String url) {
        return url + "" + token;

    }

    private void publishPasswordResetEmail(User user, String passwordResetUrl) {
        try {
            this.emailTemplateUtils.sendPasswordResetEmail(user,passwordResetUrl);
        } catch (Exception e) {
            LOGGER.error("Cannot send email to user ", e);
        }
    }
    @Transactional
    protected void deletePasswordResetToken(String token) {
        userRepository.deleteByPasswordResetToken(token);
    }
}
