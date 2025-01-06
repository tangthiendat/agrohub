package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.response.UserDTO;
import com.ttdat.authservice.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
