package com.ttdat.inventoryservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.product.SearchProductIdListQuery;
import com.ttdat.inventoryservice.api.dto.common.ProductStockDTO;
import com.ttdat.inventoryservice.api.dto.response.ProductStockPageResult;
import com.ttdat.inventoryservice.application.mappers.ProductStockMapper;
import com.ttdat.inventoryservice.application.queries.stock.GetProductStockPageQuery;
import com.ttdat.inventoryservice.application.queries.stock.GetWarehouseProductStockQuery;
import com.ttdat.inventoryservice.domain.entities.ProductStock;
import com.ttdat.inventoryservice.domain.repositories.ProductStockRepository;
import com.ttdat.inventoryservice.infrastructure.utils.PaginationUtils;
import com.ttdat.inventoryservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductStockQueryHandler {
    private final ProductStockRepository productStockRepository;
    private final ProductStockMapper productStockMapper;
    private final QueryGateway queryGateway;

    @QueryHandler
    public ProductStockPageResult handle(GetProductStockPageQuery getProductStockPageQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getProductStockPageQuery.getFilterParams();
        filterParams.put("warehouseId", warehouseId.toString());
        Pageable pageable = PaginationUtils.getPageable(getProductStockPageQuery.getPaginationParams(), getProductStockPageQuery.getSortParams());
        Specification<ProductStock> productStockSpec = getProductStockSpec(filterParams);
        Page<ProductStock> productStockPage = productStockRepository.findAll(productStockSpec, pageable);
        return ProductStockPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(productStockPage))
                .content(productStockPage.getContent().stream()
                        .map(productStock -> {
                            ProductStockDTO productStockDTO = productStockMapper.toDTO(productStock);
                            GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                                    .productId(productStock.getProductId())
                                    .build();
                            ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery, ResponseTypes.instanceOf(ProductInfo.class)).join();
                            productStockDTO.setProduct(productInfo);
                            return productStockDTO;
                        }).toList())
                .build();
    }

    private Specification<ProductStock> getProductStockSpec(Map<String, String> filterParams) {
        Specification<ProductStock> spec = Specification.where(null);
        spec = spec.and(SpecificationUtils.buildJoinSpecification(filterParams, "warehouse", "warehouseId", Long.class));
        Map<String, String> productFilterParams = new HashMap<>();
        if (filterParams.containsKey("categoryId")) {
            productFilterParams.put("categoryId", filterParams.get("categoryId"));
        }
        if (filterParams.containsKey("query")) {
            productFilterParams.put("query", filterParams.get("query"));
        }
        if (!productFilterParams.isEmpty()) {
            SearchProductIdListQuery searchProductIdListQuery = SearchProductIdListQuery.builder()
                    .filterParams(productFilterParams)
                    .build();
            List<String> productIdList = queryGateway.query(searchProductIdListQuery, ResponseTypes.multipleInstancesOf(String.class)).join();
            if (!productIdList.isEmpty()) {
                Specification<ProductStock> querySpec = (root, query, criteriaBuilder) ->
                        root.get("productId").in(productIdList);
                spec = spec.and(querySpec);
            }
        }
        return spec;
    }

    @QueryHandler
    public List<ProductStockDTO> handle(GetWarehouseProductStockQuery getWarehouseProductStockQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = new HashMap<>();
        filterParams.put("warehouseId", warehouseId.toString());
        Specification<ProductStock> productStockSpec = getProductStockSpec(filterParams);
        List<ProductStock> productStocks = productStockRepository.findAll(productStockSpec);
        return productStocks.stream()
                .map(productStock -> {
                    ProductStockDTO productStockDTO = productStockMapper.toDTO(productStock);
                    GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                            .productId(productStock.getProductId())
                            .build();
                    ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery, ResponseTypes.instanceOf(ProductInfo.class)).join();
                    productStockDTO.setProduct(productInfo);
                    return productStockDTO;
                })
                .toList();
    }
}
