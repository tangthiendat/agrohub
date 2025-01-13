package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.events.permission.PermissionCreatedEvent;
import com.ttdat.authservice.domain.events.permission.PermissionUpdatedEvent;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
    Permission toEntity(PermissionCreatedEvent permissionCreatedEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Permission permission, PermissionUpdatedEvent permissionUpdatedEvent);

    @Named("permissionIdsToEntities")
    default List<Permission> toEntities(List<Long> permissionIds){
        return permissionIds.stream()
                .map(permissionId -> Permission.builder().permissionId(permissionId).build())
                .collect(Collectors.toList());
    }
}
