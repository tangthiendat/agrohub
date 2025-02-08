package com.ttdat.productservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.productservice.api.dto.common.UnitDTO;
import com.ttdat.productservice.api.dto.response.UnitPageResult;
import com.ttdat.productservice.application.queries.unit.GetAllUnitsQuery;
import com.ttdat.productservice.application.queries.unit.GetUnitPageQuery;
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
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnitDTO>>> getUnits() {
        GetAllUnitsQuery getAllUnitsQuery = GetAllUnitsQuery.builder().build();
        List<UnitDTO> units = queryGateway.query(getAllUnitsQuery, ResponseTypes.multipleInstancesOf(UnitDTO.class)).join();
        return ResponseEntity.ok(ApiResponse.<List<UnitDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Units retrieved successfully")
                .success(true)
                .payload(units)
                .build());
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<UnitPageResult>> getUnitPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetUnitPageQuery getUnitPageQuery = GetUnitPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .build();
        UnitPageResult unitPageResult = queryGateway.query(getUnitPageQuery, ResponseTypes.instanceOf(UnitPageResult.class)).join();
        return ResponseEntity.ok(ApiResponse.<UnitPageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Unit page retrieved successfully")
                .success(true)
                .payload(unitPageResult)
                .build());
    }

}
