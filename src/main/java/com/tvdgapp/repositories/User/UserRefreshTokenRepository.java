package com.tvdgapp.repositories.User;

import com.tvdgapp.models.user.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {


    Optional<UserRefreshToken> findByToken(String token);
}
