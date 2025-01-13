package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.common.RoleDTO;
import com.ttdat.authservice.api.dto.response.RoleOption;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.authservice.domain.events.role.RoleUpdatedEvent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    List<RoleOption> toRoleOptions(List<Role> roles);

    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToEntities")
    Role toEntity(RoleCreatedEvent roleCreatedEvent);

    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToEntities")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Role role, RoleUpdatedEvent roleUpdatedEvent);

}
