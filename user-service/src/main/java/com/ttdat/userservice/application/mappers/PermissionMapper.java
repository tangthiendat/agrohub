package com.ttdat.userservice.application.mappers;

import com.ttdat.core.api.dto.response.AuthPermission;
import com.ttdat.userservice.api.dto.common.PermissionDTO;
import com.ttdat.userservice.domain.entities.Permission;
import com.ttdat.userservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.userservice.domain.events.permission.PermissionUpdatedEvent;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
    Permission toEntity(PermissionCreatedEvent permissionCreatedEvent);

    AuthPermission toAuthPermission(Permission permission);
    List<AuthPermission> toAuthPermissions(List<Permission> permissions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Permission permission, PermissionUpdatedEvent permissionUpdatedEvent);

    @Named("permissionIdsToEntities")
    default List<Permission> toEntities(List<Long> permissionIds){
        return permissionIds.stream()
                .map(permissionId -> Permission.builder().permissionId(permissionId).build())
                .collect(Collectors.toList());
    }
}
