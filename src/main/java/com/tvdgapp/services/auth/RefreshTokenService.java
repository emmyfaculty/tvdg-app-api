package com.tvdgapp.services.auth;



import com.tvdgapp.dtos.auth.RefreshTokenRequest;
import com.tvdgapp.models.user.UserRefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    String refreshToken(RefreshTokenRequest refreshTokenRequest);

    UserRefreshToken save(UserRefreshToken refreshToken);

    Optional<UserRefreshToken> findByToken(String token);

    void deleteToken(UserRefreshToken refreshToken);

    String refreshUserToken(RefreshTokenRequest refreshTokenRequest);


    UserRefreshToken createRefreshToken(String username);

}
