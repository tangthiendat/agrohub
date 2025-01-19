package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import com.ttdat.authservice.api.dto.request.FilterCriteria;
import com.ttdat.authservice.api.dto.response.PaginationMeta;
import com.ttdat.authservice.api.dto.response.PermissionPageResult;
import com.ttdat.authservice.application.mappers.PermissionMapper;
import com.ttdat.authservice.application.queries.permission.GetAllPermissionsQuery;
import com.ttdat.authservice.application.queries.permission.GetPermissionPageQuery;
import com.ttdat.authservice.domain.entities.Permission;
import com.ttdat.authservice.domain.repositories.PermissionRepository;
import com.ttdat.authservice.infrastructure.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PermissionQueryHandler {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final FilterUtils filterUtils;

    @QueryHandler
    public List<PermissionDTO> handle(GetAllPermissionsQuery getAllPermissionsQuery) {
        return permissionMapper.toDTOs(permissionRepository.findAll());
    }

    @QueryHandler
    public PermissionPageResult handle(GetPermissionPageQuery getPermissionPageQuery) {
        List<Sort.Order> sortOrders = filterUtils.toSortOrders(getPermissionPageQuery.getSortParams());
        int page = getPermissionPageQuery.getPaginationParams().getPage();
        int pageSize = getPermissionPageQuery.getPaginationParams().getPageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        Specification<Permission> spec = getPermissionPageSpec(getPermissionPageQuery.getFilterParams());
        org.springframework.data.domain.Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
        PaginationMeta paginationMeta = PaginationMeta.builder()
                .page(permissionPage.getNumber() + 1)
                .pageSize(permissionPage.getSize())
                .totalElements(permissionPage.getTotalElements())
                .totalPages(permissionPage.getTotalPages())
                .build();
        return PermissionPageResult.builder()
                .meta(paginationMeta)
                .content(permissionMapper.toDTOs(permissionPage.getContent()))
                .build();
    }

    private Specification<Permission> getPermissionPageSpec(Map<String, String> filterParams) {
        Specification<Permission> spec = Specification.where(null);
        List<FilterCriteria> methodCriteria = filterUtils.getFilterCriteria(filterParams, "httpMethod");
        List<FilterCriteria> moduleCriteria = filterUtils.getFilterCriteria(filterParams, "module");
        Specification<Permission> methodSpec = Specification.where(null);
        for (FilterCriteria criteria : methodCriteria) {
            methodSpec = methodSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("httpMethod"), criteria.getValue())));
        }
        Specification<Permission> moduleSpec = Specification.where(null);
        for (FilterCriteria criteria : moduleCriteria) {
            moduleSpec = moduleSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("module"), criteria.getValue())));
        }
        spec = spec.and(methodSpec).and(moduleSpec);
        return spec;
    }

}
