package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.response.PermissionDTO;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.events.PermissionCreatedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreatedEvent permissionCreatedEvent);
    PermissionDTO toPermissionDTO(Permission permission);
}
