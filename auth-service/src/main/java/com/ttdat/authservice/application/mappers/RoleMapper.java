package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.common.RoleDTO;
import com.ttdat.authservice.api.dto.response.RoleOption;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.authservice.domain.events.role.RoleUpdatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);

    RoleOption toRoleOption(Role role);

    Role toRole(RoleDTO roleDTO);

    @Mapping(target = "permissions", source = "permissionIds")
    Role toRole(RoleCreatedEvent roleCreatedEvent);

    @Mapping(target = "permissions", source = "permissionIds")
    void updateRoleFromEvent(@MappingTarget Role role, RoleUpdatedEvent roleUpdatedEvent);

}
