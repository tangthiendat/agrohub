package com.ttdat.authservice.api.controllers.query;

import com.ttdat.authservice.api.dto.common.PermissionDTO;
import com.ttdat.authservice.api.dto.request.PaginationParams;
import com.ttdat.authservice.api.dto.request.SortParams;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.api.dto.response.PermissionPageResult;
import com.ttdat.authservice.application.queries.permission.GetAllPermissionsQuery;
import com.ttdat.authservice.application.queries.permission.GetPermissionPageQuery;
import com.ttdat.authservice.infrastructure.utils.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionQueryController {
    private final QueryGateway queryGateway;
    private final RequestParamsUtils requestParamsUtils;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getPermissions() {
        GetAllPermissionsQuery getAllPermissionsQuery = GetAllPermissionsQuery.builder().build();
        List<PermissionDTO> permissions = queryGateway.query(getAllPermissionsQuery, ResponseTypes.multipleInstancesOf(PermissionDTO.class)).join();
        return ResponseEntity.ok(ApiResponse.<List<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Permissions retrieved successfully")
                .success(true)
                .payload(permissions)
                .build());
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PermissionPageResult>> getPermissionPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = requestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = requestParamsUtils.getSortParams(filterParams);
        GetPermissionPageQuery getPermissionPageQuery = GetPermissionPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        PermissionPageResult permissionPage = queryGateway.query(getPermissionPageQuery, ResponseTypes.instanceOf(PermissionPageResult.class)).join();
        return ResponseEntity.ok(ApiResponse.<PermissionPageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Permission page retrieved successfully")
                .success(true)
                .payload(permissionPage)
                .build());
    }
}
