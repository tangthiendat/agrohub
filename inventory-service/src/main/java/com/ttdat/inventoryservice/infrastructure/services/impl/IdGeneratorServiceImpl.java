package com.ttdat.inventoryservice.infrastructure.services.impl;

import com.ttdat.inventoryservice.infrastructure.services.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdGeneratorServiceImpl implements IdGeneratorService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long generateWarehouseId() {
        return jdbcTemplate.queryForObject("SELECT nextval('warehouses_seq')", Long.class);
    }
}
