package com.tvdgapp.repositories.User.admin;

import com.tvdgapp.models.user.admin.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {
    Optional<AccessCode> findByUserId(Long userId);
}
