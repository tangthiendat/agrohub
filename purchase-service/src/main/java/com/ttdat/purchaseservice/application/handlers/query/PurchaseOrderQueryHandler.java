package com.ttdat.purchaseservice.application.handlers.query;

import com.ttdat.purchaseservice.api.dto.response.PurchaseOrderPageResult;
import com.ttdat.purchaseservice.application.mappers.PurchaseOrderMapper;
import com.ttdat.purchaseservice.application.queries.purchaseorder.GetPurchaseOrderPageQuery;
import com.ttdat.purchaseservice.domain.entities.PurchaseOrder;
import com.ttdat.purchaseservice.domain.repositories.PurchaseOrderRepository;
import com.ttdat.purchaseservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseOrderQueryHandler {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @QueryHandler
    public PurchaseOrderPageResult handle(GetPurchaseOrderPageQuery query) {
        Pageable pageable = PaginationUtils.getPageable(query.getPaginationParams(), query.getSortParams());
        Page<PurchaseOrder> purchaseOrderPage = purchaseOrderRepository.findAll(pageable);
        return PurchaseOrderPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(purchaseOrderPage))
                .content(purchaseOrderMapper.toTableItemList(purchaseOrderPage.getContent()))
                .build();
    }
}
