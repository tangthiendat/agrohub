package com.ttdat.authservice.infrastructure.services.impl;

import com.ttdat.authservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdGeneratorServiceImpl implements IdGeneratorService {
    private final JdbcTemplate jdbcTemplate;

    public Long generatePermissionId() {
        return jdbcTemplate.queryForObject("SELECT nextval('permissions_seq')", Long.class);
    }

    public Long generateRoleId() {
        return jdbcTemplate.queryForObject("SELECT nextval('roles_seq')", Long.class);
    }
}
