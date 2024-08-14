package com.tvdgapp.services.task;

import com.tvdgapp.services.auth.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Profile("dev")
@Service
public class TokenPurgeTask {

    private final TokenBlacklistService tokenBlacklistService;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
        this.tokenBlacklistService.purgeExpiredTokens();
    }
}
