package com.ttdat.userservice.application.handlers.query;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.userservice.api.dto.response.RoleOption;
import com.ttdat.userservice.api.dto.response.RolePageResult;
import com.ttdat.userservice.application.mappers.RoleMapper;
import com.ttdat.userservice.application.queries.role.GetAllRolesQuery;
import com.ttdat.userservice.application.queries.role.GetRolePageQuery;
import com.ttdat.userservice.domain.entities.Role;
import com.ttdat.userservice.domain.repositories.RoleRepository;
import com.ttdat.userservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleQueryHandler {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @QueryHandler
    public List<RoleOption> handle(GetAllRolesQuery query) {
        return roleMapper.toRoleOptionList(roleRepository.findAll());
    }

    @QueryHandler
    public RolePageResult handle(GetRolePageQuery getRolePageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getRolePageQuery.getPaginationParams(), getRolePageQuery.getSortParams());
        Page<Role> rolePage = roleRepository.findAll(pageable);
        PaginationMeta paginationMeta = PaginationUtils.getPaginationMeta(rolePage);
        return RolePageResult.builder()
                .meta(paginationMeta)
                .content(roleMapper.toDTOs(rolePage.getContent()))
                .build();
    }

}
