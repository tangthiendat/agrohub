package com.ttdat.authservice.domain.services;

import com.ttdat.authservice.infrastructure.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final RedisService redisService;

    public void blacklistToken(String tokenKey, long expirationMillis) {
        redisService.set(tokenKey, "REVOKED", expirationMillis);
    }

    public boolean isBlacklisted(String token) {
        return redisService.hasKey("token:" + token);
    }
}
