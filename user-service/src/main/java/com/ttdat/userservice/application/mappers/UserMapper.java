package com.ttdat.userservice.application.mappers;

import com.ttdat.userservice.api.dto.common.UserDTO;
import com.ttdat.userservice.domain.entities.Role;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.events.user.UserCreatedEvent;
import com.ttdat.userservice.domain.events.user.UserUpdatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Override
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    @Mapping(target = "role.roleId", source = "roleId")
    User toEntity(UserCreatedEvent userCreatedEvent);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromEvent(@MappingTarget User user, UserUpdatedEvent userUpdatedEvent);

    @BeforeMapping
    default void updateUserRole(@MappingTarget User user, UserUpdatedEvent userUpdatedEvent) {
        if (userUpdatedEvent.getRoleId() != null) {
            Role role = user.getRole();
            if (role == null || !role.getRoleId().equals(userUpdatedEvent.getRoleId())) {
                user.setRole(Role.builder().roleId(userUpdatedEvent.getRoleId()).build());
            }
        }
    }
}
