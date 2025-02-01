package com.ttdat.userservice.application.mappers;

import com.ttdat.userservice.api.dto.common.RoleDTO;
import com.ttdat.userservice.api.dto.response.RoleOption;
import com.ttdat.userservice.domain.entities.Role;
import com.ttdat.userservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.userservice.domain.events.role.RoleUpdatedEvent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PermissionMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    List<RoleOption> toRoleOptions(List<Role> roles);

    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToEntities")
    Role toEntity(RoleCreatedEvent roleCreatedEvent);

    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToEntities")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Role role, RoleUpdatedEvent roleUpdatedEvent);

}
