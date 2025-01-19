package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.user.UserCreatedEvent;
import com.ttdat.authservice.domain.events.user.UserUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoleMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Override
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    @Mapping(target = "role.roleId", source = "roleId")
    User toEntity(UserCreatedEvent userCreatedEvent);

    @Mappings({
            @Mapping(target = "role.roleId", source = "roleId"),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role.permissions", ignore = true)
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget User user, UserUpdatedEvent userUpdatedEvent);
}
