package com.ttdat.inventoryservice.application.handlers.query;

import com.ttdat.inventoryservice.api.dto.response.ProductLocationPageResult;
import com.ttdat.inventoryservice.application.mappers.ProductLocationMapper;
import com.ttdat.inventoryservice.application.queries.location.GetProductLocationPageQuery;
import com.ttdat.inventoryservice.domain.entities.ProductLocation;
import com.ttdat.inventoryservice.domain.repositories.ProductLocationRepository;
import com.ttdat.inventoryservice.infrastructure.utils.PaginationUtils;
import com.ttdat.inventoryservice.infrastructure.utils.SpecificationUtils;
import jakarta.persistence.criteria.Expression;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductLocationQueryHandler {
    private final ProductLocationRepository productLocationRepository;
    private final ProductLocationMapper productLocationMapper;

    @QueryHandler
    public ProductLocationPageResult handle(GetProductLocationPageQuery getProductLocationPageQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getProductLocationPageQuery.getFilterParams();
        filterParams.put("warehouseId", warehouseId.toString());
        Pageable pageable = PaginationUtils.getPageable(getProductLocationPageQuery.getPaginationParams(), getProductLocationPageQuery.getSortParams());
        Specification<ProductLocation> productLocationSpec = getProductLocationSpec(filterParams);
        Page<ProductLocation> productLocationPage = productLocationRepository.findAll(productLocationSpec, pageable);
        return ProductLocationPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(productLocationPage))
                .content(productLocationMapper.toDTOList(productLocationPage.getContent()))
                .build();
    }

    private Specification<ProductLocation> getProductLocationSpec(Map<String, String> filterParams) {
        Specification<ProductLocation> spec = Specification.where(null);
        spec = spec.and(SpecificationUtils.buildJoinSpecification(filterParams, "warehouse", "warehouseId", Long.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "rackType", String.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "status", String.class));
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query");
            Specification<ProductLocation> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue.toLowerCase() + "%";
                //locationName = rackName + rowNumber + "." + columnNumber
                Expression<String> locationNameExpr = criteriaBuilder.concat(
                        root.get("rackName"),
                        criteriaBuilder.concat(
                                criteriaBuilder.concat(
                                        root.get("rowNumber").as(String.class),
                                        "."
                                ),
                                root.get("columnNumber").as(String.class)
                        )
                );
                return criteriaBuilder.like(criteriaBuilder.lower(locationNameExpr), likePattern);
            };
            spec = spec.and(querySpec);
        }
        return spec;
    }
}
