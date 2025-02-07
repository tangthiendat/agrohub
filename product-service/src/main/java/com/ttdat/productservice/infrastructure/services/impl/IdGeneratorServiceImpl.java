package com.ttdat.productservice.infrastructure.services.impl;

import com.ttdat.productservice.infrastructure.services.IdGeneratorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IdGeneratorServiceImpl implements IdGeneratorService {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall jdbcCall;

    @PostConstruct
    public void init() {
        jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("get_next_auto_increment")
                .declareParameters(
                        new SqlParameter("tbl_name", Types.VARCHAR),
                        new SqlOutParameter("next_id", Types.INTEGER)
                );
    }

    @Override
    public Long getNextId(String tableName) {
        Map<String, Object> result = jdbcCall.execute(tableName);
        return (Long) result.get("next_id");
    }

    @Transactional
    public Long generateCategoryId() {
        return this.getNextId("categories");
    }

    @Transactional
    public Long generateUnitId() {
        return getNextId("units");
    }
}
