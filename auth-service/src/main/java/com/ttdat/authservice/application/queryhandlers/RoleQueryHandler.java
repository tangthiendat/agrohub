package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.api.dto.response.PaginationMeta;
import com.ttdat.authservice.api.dto.response.RoleOption;
import com.ttdat.authservice.api.dto.response.RolePageResult;
import com.ttdat.authservice.application.mappers.RoleMapper;
import com.ttdat.authservice.application.queries.role.GetAllRolesQuery;
import com.ttdat.authservice.application.queries.role.GetRolePageQuery;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.repositories.RoleRepository;
import com.ttdat.authservice.infrastructure.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleQueryHandler {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final FilterUtils filterUtils;

    @QueryHandler
    public List<RoleOption> handle(GetAllRolesQuery query) {
        return roleMapper.toRoleOptions(roleRepository.findAll());
    }

    @QueryHandler
    public RolePageResult handle(GetRolePageQuery query) {
        List<Sort.Order> sortOrders = filterUtils.toSortOrders(query.getSortParams());
        int page = query.getPaginationParams().getPage();
        int pageSize = query.getPaginationParams().getPageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Role> rolePage = roleRepository.findAll(pageable);

        PaginationMeta paginationMeta = PaginationMeta.builder()
                .page(rolePage.getNumber() + 1)
                .pageSize(rolePage.getSize())
                .totalElements(rolePage.getTotalElements())
                .totalPages(rolePage.getTotalPages())
                .build();
        return RolePageResult.builder()
                .meta(paginationMeta)
                .content(roleMapper.toDTOs(rolePage.getContent()))
                .build();
    }

}
