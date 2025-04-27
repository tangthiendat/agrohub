package com.ttdat.userservice.infrastructure.services.impl;

import com.ttdat.userservice.application.constants.RedisKeys;
import com.ttdat.userservice.infrastructure.services.RedisKeyService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisKeyServiceImpl implements RedisKeyService {
    @Override
    public String getUserRoleKey(String userId) {
        return RedisKeys.USER_PREFIX + ":" + userId + ":role";
    }
}
