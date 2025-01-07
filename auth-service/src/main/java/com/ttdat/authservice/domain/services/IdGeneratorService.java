package com.ttdat.authservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdGeneratorService {
    private final JdbcTemplate jdbcTemplate;

    public Long generatePermissionId() {
        return jdbcTemplate.queryForObject("SELECT nextval('permissions_seq')", Long.class);
    }
}
