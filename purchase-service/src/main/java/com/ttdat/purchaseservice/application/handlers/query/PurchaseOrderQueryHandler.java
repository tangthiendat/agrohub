package com.ttdat.purchaseservice.application.handlers.query;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.purchaseservice.api.dto.common.PurchaseOrderDTO;
import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderPageResult;
import com.ttdat.purchaseservice.application.mappers.PurchaseOrderMapper;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderByIdQuery;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderPageQuery;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.repositories.PurchaseOrderRepository;
import com.ttdat.purchaseservice.infrastructure.utils.PaginationUtils;
import com.ttdat.purchaseservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PurchaseOrderQueryHandler {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @QueryHandler
    public PurchaseOrderPageResult handle(GetPurchaseOrderPageQuery getPurchaseOrderPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getPurchaseOrderPageQuery.getPaginationParams(), getPurchaseOrderPageQuery.getSortParams());
        Specification<PurchaseOrder> purchaseOrderSpec = getPurchaseOrderSpec(getPurchaseOrderPageQuery.getFilterParams());
        Page<PurchaseOrder> purchaseOrderPage = purchaseOrderRepository.findAll(purchaseOrderSpec, pageable);
        return PurchaseOrderPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(purchaseOrderPage))
                .content(purchaseOrderMapper.toTableItemList(purchaseOrderPage.getContent()))
                .build();
    }

    public Specification<PurchaseOrder> getPurchaseOrderSpec(Map<String, String> filterParams) {
        Specification<PurchaseOrder> purchaseOrderSpec = Specification.where(null);
        purchaseOrderSpec = purchaseOrderSpec.and(SpecificationUtils.buildSpecification(filterParams, "status", String.class));
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query");
            Specification<PurchaseOrder> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("purchaseOrderId"), likePattern),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("supplier").get("supplierName"))),
                                likePattern)
                );
            };
            purchaseOrderSpec = purchaseOrderSpec.and(querySpec);
        }
        return purchaseOrderSpec;
    }

    @QueryHandler
    public PurchaseOrderDTO handle(GetPurchaseOrderByIdQuery getPurchaseOrderByIdQuery){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(getPurchaseOrderByIdQuery.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
        return purchaseOrderMapper.toDTO(purchaseOrder);
    }
}
