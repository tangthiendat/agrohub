package com.ttdat.userservice.infrastructure.services;

public interface IdGeneratorService {
    Long generatePermissionId();

    Long generateRoleId();

    Long getNextId(String tableName);
}
