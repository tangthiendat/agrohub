package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.response.PermissionDTO;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.authservice.domain.events.permission.PermissionUpdatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreatedEvent permissionCreatedEvent);
    PermissionDTO toPermissionDTO(Permission permission);
    void updatePermissionFromEvent(@MappingTarget Permission permission, PermissionUpdatedEvent permissionUpdatedEvent);
}
