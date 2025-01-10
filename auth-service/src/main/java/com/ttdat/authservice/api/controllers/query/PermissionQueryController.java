package com.ttdat.authservice.api.controllers.query;

import com.ttdat.authservice.api.dto.PermissionDTO;
import com.ttdat.authservice.api.dto.response.ApiResponse;
import com.ttdat.authservice.application.queries.FindAllPermissionQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> findAllPermissions() {
        List<PermissionDTO> permissions = queryGateway.query(new FindAllPermissionQuery(), ResponseTypes.multipleInstancesOf(PermissionDTO.class)).join();
        return ResponseEntity.ok(ApiResponse.<List<PermissionDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Permissions retrieved successfully")
                .success(true)
                .payload(permissions)
                .build());
    }
}
