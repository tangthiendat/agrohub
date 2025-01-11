package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import com.ttdat.authservice.application.mappers.PermissionMapper;
import com.ttdat.authservice.application.queries.GetAllPermissionsQuery;
import com.ttdat.authservice.domain.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionQueryHandler {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @QueryHandler
    public List<PermissionDTO> handle(GetAllPermissionsQuery query) {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionDTO)
                .collect(Collectors.toList());
    }
}
