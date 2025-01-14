package com.ttdat.authservice.infrastructure.services;

public interface RedisService {
    void set(String key, Object value);
    void set(String key, Object value, long timeoutMillis);
    Object get(String key);
    boolean hasKey(String key);
    boolean delete(String key);
}
