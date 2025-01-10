package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.RoleDTO;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.events.role.RoleCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);

    Role toRole(RoleDTO roleDTO);

    @Mapping(target = "permissions", source = "permissionIds")
    Role toRole(RoleCreatedEvent roleCreatedEvent);

}
