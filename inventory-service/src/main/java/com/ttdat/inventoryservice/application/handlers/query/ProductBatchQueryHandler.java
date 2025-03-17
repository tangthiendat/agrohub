package com.ttdat.inventoryservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.inventoryservice.api.dto.common.ProductBatchDTO;
import com.ttdat.inventoryservice.api.dto.response.ProductBatchPageResult;
import com.ttdat.inventoryservice.application.mappers.ProductBatchMapper;
import com.ttdat.inventoryservice.application.queries.batch.GetProductBatchPageQuery;
import com.ttdat.inventoryservice.domain.entities.ProductBatch;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.repositories.ProductBatchRepository;
import com.ttdat.inventoryservice.infrastructure.utils.PaginationUtils;
import com.ttdat.inventoryservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductBatchQueryHandler {
    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchMapper productBatchMapper;
    private final QueryGateway queryGateway;

    @QueryHandler
    public ProductBatchPageResult handle(GetProductBatchPageQuery getProductBatchPageQuery) {
        Map<String, String> filterParams = getProductBatchPageQuery.getFilterParams();
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
        spec = spec.and(SpecificationUtils.buildSpecification(filterParams, "warehouseId", Long.class));
        return spec;
    }
}
