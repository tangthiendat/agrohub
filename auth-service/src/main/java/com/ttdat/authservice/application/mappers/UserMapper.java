package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.user.UserCreatedEvent;
import com.ttdat.authservice.domain.events.user.UserUpdatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role.permissions", ignore = true)
    })
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    @Mapping(target = "role.roleId", source = "roleId")
    User toUser(UserCreatedEvent userCreatedEvent);

    @Mappings({
            @Mapping(target = "role.roleId", source = "roleId"),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role.permissions", ignore = true)
    })
    void updateUserFromEvent(@MappingTarget User user, UserUpdatedEvent userUpdatedEvent);
}
