package com.ttdat.authservice.api.controllers.query;

import com.ttdat.authservice.api.dto.request.PaginationParams;
import com.ttdat.authservice.api.dto.request.SortParams;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.api.dto.response.UserPageResult;
import com.ttdat.authservice.application.queries.user.GetUserPageQuery;
import com.ttdat.authservice.infrastructure.utils.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserQueryController {
    private final QueryGateway queryGateway;
    private final RequestParamsUtils requestParamsUtils;

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<UserPageResult>> getUserPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = requestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = requestParamsUtils.getSortParams(filterParams);
        GetUserPageQuery getUserPageQuery = GetUserPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        UserPageResult users = queryGateway.query(getUserPageQuery, ResponseTypes.instanceOf(UserPageResult.class)).join();
        return ResponseEntity.ok(ApiResponse.<UserPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User page fetched successfully")
                .payload(users)
                .build());
    }
}
