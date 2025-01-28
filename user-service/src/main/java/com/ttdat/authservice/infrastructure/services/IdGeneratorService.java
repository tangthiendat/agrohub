package com.ttdat.authservice.infrastructure.services;

public interface IdGeneratorService {
    Long generatePermissionId();

    Long generateRoleId();

    Long getNextId(String tableName);
}
