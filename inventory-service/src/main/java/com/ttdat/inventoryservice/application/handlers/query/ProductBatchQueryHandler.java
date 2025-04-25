package com.ttdat.inventoryservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductBatchInfo;
import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.application.queries.inventory.GetProductBatchInfoByIdQuery;
import com.ttdat.core.application.queries.inventory.GetProductCurrentStockQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.product.SearchProductIdListQuery;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.api.dto.response.ProductBatchPageResult;
import com.ttdat.inventoryservice.application.mappers.ProductBatchMapper;
import com.ttdat.inventoryservice.application.queries.batch.GetAllProductBatchQuery;
import com.ttdat.inventoryservice.application.queries.batch.GetProductBatchPageQuery;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.entities.ProductBatchLocation;
import com.ttdat.inventoryservice.domain.repositories.ProductBatchRepository;
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
public class ProductBatchQueryHandler {
    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchMapper productBatchMapper;
    private final QueryGateway queryGateway;

    @QueryHandler
    public ProductBatchPageResult handle(GetProductBatchPageQuery getProductBatchPageQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getProductBatchPageQuery.getFilterParams();
        filterParams.put("warehouseId", warehouseId.toString());
        Specification<ProductBatch> productBatchSpec = getProductBatchSpec(filterParams);
        Pageable pageable = PaginationUtils.getPageable(getProductBatchPageQuery.getPaginationParams(), getProductBatchPageQuery.getSortParams());
        Page<ProductBatch> productBatchPage = productBatchRepository.findAll(productBatchSpec, pageable);
        return ProductBatchPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(productBatchPage))
                .content(productBatchPage.getContent().stream()
                        .map(productBatch -> {
                            ProductBatchDTO productBatchDTO = productBatchMapper.toDTO(productBatch);
                            GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                                    .productId(productBatch.getProductId())
                                    .build();
                            ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery, ResponseTypes.instanceOf(ProductInfo.class)).join();
                            productBatchDTO.setProduct(productInfo);
                            return productBatchDTO;
                        }).toList())
                .build();
    }

    private Specification<ProductBatch> getProductBatchSpec(Map<String, String> filterParams) {
        Specification<ProductBatch> spec = Specification.where(null);
        spec = spec.and(SpecificationUtils.buildJoinSpecification(filterParams, "warehouse", "warehouseId", Long.class));
//                .and(SpecificationUtils.buildSpecification(filterParams, "productId", String.class));
        Map<String, String> productFilterParams = new HashMap<>();
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query");
            Specification<ProductBatch> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.like(root.get("batchId"), likePattern);
            };
            productFilterParams.put("query", filterParams.get("query"));
            SearchProductIdListQuery searchProductIdListQuery = SearchProductIdListQuery.builder()
                    .filterParams(productFilterParams)
                    .build();
            List<String> productIdList = queryGateway.query(searchProductIdListQuery, ResponseTypes.multipleInstancesOf(String.class)).join();
            if (!productIdList.isEmpty()) {
                Specification<ProductBatch> productIdSpec = (root, query, criteriaBuilder) ->
                        root.get("productId").in(productIdList);
                querySpec = querySpec.or(productIdSpec);
            }
            spec = spec.and(querySpec);
        }
        return spec;
    }

    @QueryHandler
    public List<ProductBatchDTO> handle(GetAllProductBatchQuery getAllProductBatchQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getAllProductBatchQuery.getFilterParams();
        String productId = filterParams.get("productId");
        List<ProductBatch> productBatches = productBatchRepository.findByWarehouseIdAndProductId(warehouseId, productId);
        return productBatches.stream()
                .filter(productBatch -> !productBatch.getBatchLocations().isEmpty())
                .map(productBatchMapper::toDTO)
                .toList();
    }

    @QueryHandler
    public ProductBatchInfo handle(GetProductBatchInfoByIdQuery getProductBatchInfoByIdQuery, QueryMessage<?, ?> queryMessage){
        Map<String, String> filterParams = new HashMap<>();
        filterParams.put("warehouseId", getProductBatchInfoByIdQuery.getWarehouseId().toString());
        Specification<ProductBatch> productBatchSpec = getProductBatchSpec(filterParams);
        List<ProductBatch> productBatches = productBatchRepository.findAll(productBatchSpec);
        ProductBatch productBatch = productBatches.stream()
                .filter(batch -> batch.getBatchId().equals(getProductBatchInfoByIdQuery.getProductBatchId()))
                .findFirst()
                .orElse(null);
        return productBatchMapper.toProductBatchInfo(productBatch);
    }

    @QueryHandler
    public Double handle(GetProductCurrentStockQuery getProductCurrentStockQuery){
        List<ProductBatch> productBatches = productBatchRepository.findByWarehouseIdAndProductId(getProductCurrentStockQuery.getWarehouseId(), getProductCurrentStockQuery.getProductId());
        if (productBatches.isEmpty()) {
            return 0.0;
        }
        return productBatches.stream()
                .filter(productBatch -> !productBatch.getBatchLocations().isEmpty())
                .map(productBatch -> productBatch.getBatchLocations().stream()
                        .mapToDouble(ProductBatchLocation::getQuantity)
                        .sum())
                .reduce(0.0, Double::sum);
    }
}
