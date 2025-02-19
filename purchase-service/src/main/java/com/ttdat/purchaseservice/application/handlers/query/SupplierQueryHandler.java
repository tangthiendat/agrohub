package com.ttdat.purchaseservice.application.handlers.query;

import com.ttdat.purchaseservice.api.dto.response.SupplierPageResult;
import com.ttdat.purchaseservice.application.mappers.SupplierMapper;
import com.ttdat.purchaseservice.application.queries.supplier.GetSupplierPageQuery;
import com.ttdat.purchaseservice.domain.entities.Supplier;
import com.ttdat.purchaseservice.domain.repositories.SupplierRepository;
import com.ttdat.purchaseservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierQueryHandler {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @QueryHandler
    public SupplierPageResult handle(GetSupplierPageQuery query) {
        Pageable pageable = PaginationUtils.getPageable(query.getPaginationParams(), query.getSortParams());
        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);
        return SupplierPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(supplierPage))
                .content(supplierMapper.toDTOList(supplierPage.getContent()))
                .build();
    }

}
