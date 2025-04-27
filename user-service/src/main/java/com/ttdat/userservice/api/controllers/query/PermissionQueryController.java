package com.ttdat.userservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.userservice.api.dto.common.PermissionDTO;
import com.ttdat.userservice.api.dto.response.PermissionPageResult;
import com.ttdat.userservice.application.queries.permission.GetAllPermissionsQuery;
import com.ttdat.userservice.application.queries.permission.GetPermissionPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public ApiResponse<List<PermissionDTO>> getPermissions() {
        GetAllPermissionsQuery getAllPermissionsQuery = GetAllPermissionsQuery.builder().build();
        List<PermissionDTO> permissions = queryGateway.query(getAllPermissionsQuery, ResponseTypes.multipleInstancesOf(PermissionDTO.class)).join();
        return ApiResponse.<List<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Permissions retrieved successfully")
                .success(true)
                .payload(permissions)
                .build();
    }

    @GetMapping("/page")
    public ApiResponse<PermissionPageResult> getPermissionPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPermissionPageQuery getPermissionPageQuery = GetPermissionPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        PermissionPageResult permissionPage = queryGateway.query(getPermissionPageQuery, ResponseTypes.instanceOf(PermissionPageResult.class)).join();
        return ApiResponse.<PermissionPageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Permission page retrieved successfully")
                .success(true)
                .payload(permissionPage)
                .build();
    }
}
