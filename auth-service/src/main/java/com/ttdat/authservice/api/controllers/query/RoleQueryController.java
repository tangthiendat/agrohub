package com.ttdat.authservice.api.controllers.query;

import com.ttdat.authservice.api.dto.request.PaginationParams;
import com.ttdat.authservice.api.dto.request.SortParams;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.api.dto.response.RoleOption;
import com.ttdat.authservice.api.dto.response.RolePageResult;
import com.ttdat.authservice.application.queries.GetAllRolesQuery;
import com.ttdat.authservice.application.queries.GetRolePageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleOption>>> getRoles() {
        GetAllRolesQuery getAllRolesQuery = GetAllRolesQuery.builder().build();
        List<RoleOption> roles = queryGateway.query(getAllRolesQuery, ResponseTypes.multipleInstancesOf(RoleOption.class)).join();
        return ResponseEntity.ok(ApiResponse.<List<RoleOption>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Roles fetched successfully")
                .payload(roles)
                .build());
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<RolePageResult>> getRolePage(@ModelAttribute PaginationParams paginationParams,
                                                                   @ModelAttribute SortParams sortParams,
                                                                   @ModelAttribute Map<String, String> filterParams) {
        GetRolePageQuery getRolePageQuery = GetRolePageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        RolePageResult roles = queryGateway.query(getRolePageQuery, ResponseTypes.instanceOf(RolePageResult.class)).join();
        return ResponseEntity.ok(ApiResponse.<RolePageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Role page fetched successfully")
                .payload(roles)
                .build());
    }
}
