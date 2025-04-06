package com.ttdat.inventoryservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.api.dto.response.ProductBatchPageResult;
import com.ttdat.inventoryservice.application.mappers.ProductBatchMapper;
import com.ttdat.inventoryservice.application.queries.batch.GetAllProductBatchQuery;
import com.ttdat.inventoryservice.application.queries.batch.GetProductBatchPageQuery;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
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
        spec = spec.and(SpecificationUtils.buildJoinSpecification(filterParams, "warehouse", "warehouseId", Long.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "productId", String.class));
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query");
            Specification<ProductBatch> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.like(root.get("batchId"), likePattern);
            };
            spec = spec.and(querySpec);
        }
        return spec;
    }

    @QueryHandler
    public List<ProductBatchDTO> getAllProductBatches(GetAllProductBatchQuery getAllProductBatchQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getAllProductBatchQuery.getFilterParams();
        filterParams.put("warehouseId", warehouseId.toString());
        Specification<ProductBatch> productBatchSpec = getProductBatchSpec(filterParams);
        List<ProductBatch> productBatches = productBatchRepository.findAll(productBatchSpec);
        return productBatches.stream()
                .map(productBatchMapper::toDTO)
                .toList();
    }
}
