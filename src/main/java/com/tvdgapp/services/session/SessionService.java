package com.tvdgapp.services.session;

import com.tvdgapp.models.session.ApiSession;
import com.tvdgapp.repositories.session.ApiSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private ApiSessionRepository apiSessionRepository;

    public void refreshSession(String token) {
        Optional<ApiSession> apiSessionOptional = apiSessionRepository.findByToken(token);
        if (apiSessionOptional.isPresent()) {
            ApiSession apiSession = apiSessionOptional.get();
            apiSession.setExpiresAt(new Date(new Date().getTime() + 86400000)); // Extend session for 1 hour
            apiSessionRepository.save(apiSession);
        }
    }

    public void revokeSession(String token) {
        Optional<ApiSession> apiSessionOptional = apiSessionRepository.findByToken(token);
        if (apiSessionOptional.isPresent()) {
            ApiSession apiSession = apiSessionOptional.get();
            apiSession.setActive(false);
            apiSession.setRevokedAt(new Date());
            apiSessionRepository.save(apiSession);
        }
    }
}
