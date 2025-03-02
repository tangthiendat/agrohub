package com.ttdat.userservice.application.mappers;

import com.ttdat.core.api.dto.response.AuthRole;
import com.ttdat.userservice.api.dto.common.RoleDTO;
import com.ttdat.userservice.api.dto.response.RoleOption;
import com.ttdat.userservice.domain.entities.Role;
import com.ttdat.userservice.domain.events.role.RoleCreatedEvent;
import com.ttdat.userservice.domain.events.role.RoleUpdatedEvent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {PermissionMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    RoleOption toRoleOption(Role role);

    List<RoleOption> toRoleOptionList(List<Role> roles);

    AuthRole toAuthRole(Role role);

    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToEntities")
    Role toEntity(RoleCreatedEvent roleCreatedEvent);

    @Mapping(target = "permissions", source = "permissionIds", qualifiedByName = "permissionIdsToEntities")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget Role role, RoleUpdatedEvent roleUpdatedEvent);

}
