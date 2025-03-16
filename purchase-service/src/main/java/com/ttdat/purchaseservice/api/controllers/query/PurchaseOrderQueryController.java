package com.ttdat.purchaseservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.api.dto.response.UserInfo;
import com.ttdat.core.api.dto.response.WarehouseInfo;
import com.ttdat.core.application.queries.inventory.GetWarehouseInfoByIdQuery;
import com.ttdat.core.application.queries.user.GetUserInfoByIdQuery;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.purchaseservice.api.dto.common.PurchaseOrderDTO;
import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderListItem;
import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderPageResult;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetAllPurchaseOrderQuery;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderByIdQuery;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderPageQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public ApiResponse<List<PurchaseOrderListItem>> getPurchaseOrders(@RequestParam Map<String, String> filterParams) {
        GetAllPurchaseOrderQuery getAllPurchaseOrderQuery = GetAllPurchaseOrderQuery.builder()
                .filterParams(filterParams)
                .build();
        List<PurchaseOrderListItem> purchaseOrderListItems = queryGateway.query(getAllPurchaseOrderQuery, ResponseTypes.multipleInstancesOf(PurchaseOrderListItem.class)).join();
        return ApiResponse.<List<PurchaseOrderListItem>>builder()
                .status(HttpStatus.OK.value())
                .message("Get purchase orders successfully")
                .success(true)
                .payload(purchaseOrderListItems)
                .build();
    }

    @GetMapping("/page")
    public ApiResponse<PurchaseOrderPageResult> getPurchaseOrderPage(@RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPurchaseOrderPageQuery getPurchaseOrderPageQuery = GetPurchaseOrderPageQuery.builder()
                .paginationParams(paginationParams)
                .sortParams(sortParams)
                .filterParams(filterParams)
                .build();
        PurchaseOrderPageResult purchaseOrderPageResult = queryGateway.query(getPurchaseOrderPageQuery, ResponseTypes.instanceOf(PurchaseOrderPageResult.class)).join();
        return ApiResponse.<PurchaseOrderPageResult>builder()
                .status(HttpStatus.OK.value())
                .message("Get purchase order page successfully")
                .success(true)
                .payload(purchaseOrderPageResult)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PurchaseOrderDTO> getPurchaseOrderById(@PathVariable("id") String id) {
        GetPurchaseOrderByIdQuery getPurchaseOrderByIdQuery = GetPurchaseOrderByIdQuery.builder()
                .purchaseOrderId(id)
                .build();
        PurchaseOrderDTO purchaseOrderDTO = queryGateway.query(getPurchaseOrderByIdQuery, ResponseTypes.instanceOf(PurchaseOrderDTO.class)).join();
        GetUserInfoByIdQuery getUserInfoByIdQuery = GetUserInfoByIdQuery.builder()
                .userId(purchaseOrderDTO.getUser().getUserId())
                .build();
        UserInfo userInfo = queryGateway.query(getUserInfoByIdQuery, ResponseTypes.instanceOf(UserInfo.class)).join();
        purchaseOrderDTO.setUser(userInfo);
        GetWarehouseInfoByIdQuery getWarehouseInfoByIdQuery = GetWarehouseInfoByIdQuery.builder()
                .warehouseId(purchaseOrderDTO.getWarehouse().getWarehouseId())
                .build();
        WarehouseInfo warehouseInfo = queryGateway.query(getWarehouseInfoByIdQuery, ResponseTypes.instanceOf(WarehouseInfo.class)).join();
        purchaseOrderDTO.setWarehouse(warehouseInfo);
        return ApiResponse.<PurchaseOrderDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Get purchase order successfully")
                .success(true)
                .payload(purchaseOrderDTO)
                .build();
    }
}
