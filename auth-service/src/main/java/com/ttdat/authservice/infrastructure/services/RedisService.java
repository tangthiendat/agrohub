package com.ttdat.authservice.infrastructure.services;

public interface RedisService {
    void set(String key, Object value);
    void set(String key, Object value, int timeout);
    Object get(String key);
    boolean delete(String key);
}
