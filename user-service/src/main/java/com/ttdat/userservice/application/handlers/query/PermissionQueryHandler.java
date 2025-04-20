package com.ttdat.userservice.application.handlers.query;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.userservice.api.dto.common.PermissionDTO;
import com.ttdat.userservice.api.dto.response.PermissionPageResult;
import com.ttdat.userservice.application.mappers.PermissionMapper;
import com.ttdat.userservice.application.queries.permission.GetAllPermissionsQuery;
import com.ttdat.userservice.application.queries.permission.GetPermissionPageQuery;
import com.ttdat.userservice.domain.entities.Permission;
import com.ttdat.userservice.domain.repositories.PermissionRepository;
import com.ttdat.userservice.infrastructure.utils.PaginationUtils;
import com.ttdat.userservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PermissionQueryHandler {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

@QueryHandler
public List<PermissionDTO> handle(GetAllPermissionsQuery getAllPermissionsQuery) {
    return permissionMapper.toDTOs(permissionRepository.findAll());
}

    @QueryHandler
    public PermissionPageResult handle(GetPermissionPageQuery getPermissionPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getPermissionPageQuery.getPaginationParams(), getPermissionPageQuery.getSortParams());
        Specification<Permission> permissionPageSpec = getPermissionPageSpec(getPermissionPageQuery.getFilterParams());
        Page<Permission> permissionPage = permissionRepository.findAll(permissionPageSpec, pageable);
        PaginationMeta paginationMeta = PaginationUtils.getPaginationMeta(permissionPage);
        return PermissionPageResult.builder()
                .meta(paginationMeta)
                .content(permissionMapper.toDTOs(permissionPage.getContent()))
                .build();
    }

    private Specification<Permission> getPermissionPageSpec(Map<String, String> filterParams) {
        Specification<Permission> permissionPageSpec = Specification.where(null);
        permissionPageSpec = permissionPageSpec.and(SpecificationUtils.buildSpecification(filterParams, "httpMethod", String.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "module", String.class));
        return permissionPageSpec;
    }

}
