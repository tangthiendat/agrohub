package com.ttdat.userservice.infrastructure.services;

import java.util.UUID;

public interface RedisKeyService {
    String getUserRoleKey(UUID userId);
}
