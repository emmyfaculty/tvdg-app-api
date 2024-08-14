package com.tvdgapp.repositories.session;

import com.tvdgapp.models.session.ApiSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiSessionRepository extends JpaRepository<ApiSession, Long> {

    Optional<ApiSession> findByToken(String token);

    Optional<ApiSession> findByUserIdAndToken(Long userId, String token);

    void deleteByToken(String token);
}