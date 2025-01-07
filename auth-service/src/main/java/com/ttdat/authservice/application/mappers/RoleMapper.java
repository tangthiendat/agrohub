package com.ttdat.authservice.application.mappers;

import com.ttdat.authservice.api.dto.response.RoleDTO;
import com.ttdat.authservice.domain.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);

    Role toRole(RoleDTO roleDTO);
}
