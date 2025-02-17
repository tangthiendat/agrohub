package com.ttdat.productservice.infrastructure.services;

public interface IdGeneratorService {
    Long generateCategoryId();

    Long generateUnitId();

    Long getNextId(String tableName);
}
