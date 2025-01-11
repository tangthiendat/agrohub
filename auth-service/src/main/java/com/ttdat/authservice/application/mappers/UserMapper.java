package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.user.UserCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    @Mapping(target = "role.roleId", source = "roleId")
    User toUser(UserCreatedEvent userCreatedEvent);
}
