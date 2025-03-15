package com.ttdat.purchaseservice.application.handlers.query;

import com.ttdat.purchaseservice.api.dto.common.SupplierDTO;
import com.ttdat.purchaseservice.api.dto.response.SupplierPageResult;
import com.ttdat.purchaseservice.application.mappers.SupplierMapper;
import com.ttdat.purchaseservice.application.mappers.SupplierRatingMapper;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierByProductIdQuery;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierPageQuery;
import com.ttdat.purchaseservice.application.queries.supplier.SearchSupplierQuery;
import com.ttdat.purchaseservice.domain.entities.Supplier;
import com.ttdat.purchaseservice.domain.repositories.SupplierRepository;
import com.ttdat.purchaseservice.infrastructure.utils.PaginationUtils;
import com.ttdat.purchaseservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SupplierQueryHandler {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierRatingMapper supplierRatingMapper;

    private List<SupplierDTO> toDTOList(List<Supplier> suppliers, Long warehouseId) {
        return suppliers.stream().map(supplier -> {
            SupplierDTO supplierDTO = supplierMapper.toDTO(supplier);
            supplier.getSupplierRatings().stream()
                    .filter(supplierRating -> Objects.equals(supplierRating.getWarehouseId(), warehouseId))
                    .findFirst()
                    .ifPresent(currentSupplierRating -> supplierDTO.setSupplierRating(supplierRatingMapper.toDTO(currentSupplierRating)));
            return supplierDTO;
        }).toList();
    }

    @QueryHandler
    public SupplierPageResult handle(GetSupplierPageQuery query, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Pageable pageable = PaginationUtils.getPageable(query.getPaginationParams(), query.getSortParams());
        Specification<Supplier> supplierPageSpec = getSupplierSpec(query.getFilterParams());
        Page<Supplier> supplierPage = supplierRepository.findAll(supplierPageSpec, pageable);
        return SupplierPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(supplierPage))
                .content(toDTOList(supplierPage.getContent(), warehouseId))
                .build();
    }

    private Specification<Supplier> getSupplierSpec(Map<String, String> filterParams) {
        Specification<Supplier> supplierSpec = Specification.where(null);
        supplierSpec = supplierSpec.and(SpecificationUtils.buildSpecification(filterParams, "active", Boolean.class));
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query").toLowerCase();
            Specification<Supplier> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("supplierId"), likePattern),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("supplierName"))),
                                likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), likePattern)
                );
            };
            supplierSpec = supplierSpec.and(querySpec);
        }
        return supplierSpec;
    }

    @QueryHandler
    public List<SupplierDTO> handle(SearchSupplierQuery searchSupplierQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Specification<Supplier> supplierSpec = getSupplierSpec(Map.of("query", searchSupplierQuery.getQuery()));
        List<Supplier> suppliers = supplierRepository.findAll(supplierSpec);
        return toDTOList(suppliers, warehouseId);
    }

    @QueryHandler
    public List<SupplierDTO> handle(GetSupplierByProductIdQuery query, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        List<Supplier> suppliers = supplierRepository.findByProductId(query.getProductId());
        return toDTOList(suppliers, warehouseId);
    }

}
