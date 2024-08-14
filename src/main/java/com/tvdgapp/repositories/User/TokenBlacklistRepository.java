package com.tvdgapp.repositories.User;


import com.tvdgapp.models.user.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

     boolean existsByToken(String token);
}
