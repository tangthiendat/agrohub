package com.ttdat.userservice.infrastructure.services;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void set(String key, Object value);
    void set(String key, Object value, long timeout, TimeUnit timeUnit);
    Object get(String key);
    boolean hasKey(String key);
    boolean delete(String key);
}
