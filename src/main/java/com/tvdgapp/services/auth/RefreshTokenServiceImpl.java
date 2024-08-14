package com.tvdgapp.services.auth;

import com.tvdgapp.dtos.auth.RefreshTokenRequest;
import com.tvdgapp.exceptions.InvalidRefreshTokenException;
import com.tvdgapp.models.user.UserRefreshToken;
import com.tvdgapp.models.user.UserType;
import com.tvdgapp.repositories.User.UserRefreshTokenRepository;
import com.tvdgapp.securityconfig.JwtTokenProvider;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.utils.CodeGeneratorUtils;
import com.tvdgapp.utils.TvdgAppDateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider authTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public String refreshToken(RefreshTokenRequest refreshTokenRequest) {
        this.validateToken(refreshTokenRequest);
        return authTokenProvider.generateToken(refreshTokenRequest.getUserName());
    }


    private void validateToken(RefreshTokenRequest refreshTokenRequest) {
        Optional<UserRefreshToken> optional = this.refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken());

        if (optional.isEmpty()) {
            throw new InvalidRefreshTokenException("Token does not exist");
        }

        if (expiredToken(optional.get())) {
            throw new InvalidRefreshTokenException("Token has expired");
        }

        if (invalidTokenIssued(optional.get(), refreshTokenRequest.getUserName())) {
            throw new InvalidRefreshTokenException("Invalid Token issued");
        }
    }

    private boolean invalidTokenIssued(UserRefreshToken refreshToken, String requestUserName) {
        if (!StringUtils.equals(refreshToken.getUserName(), requestUserName)) {
            return true;
        }
        return false;
    }

    @Override
    public UserRefreshToken save(UserRefreshToken refreshToken) {
        return this.refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<UserRefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(UserRefreshToken refreshToken) {
        this.refreshTokenRepository.delete( refreshToken);
    }

    @Override
    public String refreshUserToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            this.validateToken(refreshTokenRequest);
            SecuredUserInfo userDetails = (SecuredUserInfo) this.userDetailsService.loadUserByUsername(refreshTokenRequest.getUserName());
            return authTokenProvider.generateToken(refreshTokenRequest.getUserName());
        } catch (UsernameNotFoundException e) {
            throw new InvalidRefreshTokenException("Invalid username");
        }
    }

    @Override
    public UserRefreshToken createRefreshToken(String userName) {

        UserRefreshToken refreshToken =this.createRefreshTokenModel(userName);
        this.save(refreshToken);
        return refreshToken;
    }

    private UserRefreshToken createRefreshTokenModel(String userName) {
        UserRefreshToken refreshToken = new UserRefreshToken();
        refreshToken.setUserName(userName);
        int validityTrm = 10080;//mins(7days) todo:should come from configuration
        refreshToken.calculateExpiryDate(String.valueOf(validityTrm));
        refreshToken.setToken(CodeGeneratorUtils.generateRefreshToken());
        return refreshToken;
    }


    private boolean expiredToken(UserRefreshToken customerRefreshToken) {
        Date now = TvdgAppDateUtils.now();
        if (customerRefreshToken.getValidityTrm()!=null&&customerRefreshToken.getValidityTrm().before(now)) {
            return true;
        }
        return false;
    }
}
