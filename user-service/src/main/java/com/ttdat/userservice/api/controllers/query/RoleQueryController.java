package com.ttdat.userservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.userservice.api.dto.response.RoleOption;
import com.ttdat.userservice.api.dto.response.RolePageResult;
import com.ttdat.userservice.application.queries.role.GetAllRolesQuery;
import com.ttdat.userservice.application.queries.role.GetRolePageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public ApiResponse<List<RoleOption>> getRoles() {
        GetAllRolesQuery getAllRolesQuery = GetAllRolesQuery.builder().build();
        List<RoleOption> roles = queryGateway.query(getAllRolesQuery, ResponseTypes.multipleInstancesOf(RoleOption.class)).join();
        return ApiResponse.<List<RoleOption>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Roles fetched successfully")
                .payload(roles)
                .build();
    }

    @GetMapping("/page")
    public ApiResponse<RolePageResult> getRolePage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetRolePageQuery getRolePageQuery = GetRolePageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        RolePageResult roles = queryGateway.query(getRolePageQuery, ResponseTypes.instanceOf(RolePageResult.class)).join();
        return ApiResponse.<RolePageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Role page fetched successfully")
                .payload(roles)
                .build();
    }
}
