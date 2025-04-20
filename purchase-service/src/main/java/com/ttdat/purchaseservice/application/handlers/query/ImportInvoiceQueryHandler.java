package com.ttdat.purchaseservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.api.dto.response.ProductUnitInfo;
import com.ttdat.core.api.dto.response.UserInfo;
import com.ttdat.core.api.dto.response.WarehouseInfo;
import com.ttdat.core.application.queries.inventory.GetWarehouseInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.stats.GetImportInvoiceCountQuery;
import com.ttdat.core.application.queries.user.GetUserInfoByIdQuery;
import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDTO;
import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDetailDTO;
import com.ttdat.purchaseservice.api.dto.response.ImportInvoicePageResult;
import com.ttdat.purchaseservice.application.mappers.ImportInvoiceDetailMapper;
import com.ttdat.purchaseservice.application.mappers.ImportInvoiceMapper;
import com.ttdat.purchaseservice.application.queries.importinvoice.GetImportInvoicePageQuery;
import com.ttdat.purchaseservice.domain.entities.ImportInvoice;
import com.ttdat.purchaseservice.domain.repositories.ImportInvoiceRepository;
import com.ttdat.purchaseservice.infrastructure.utils.PaginationUtils;
import com.ttdat.purchaseservice.infrastructure.utils.SpecificationUtils;
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
public class ImportInvoiceQueryHandler {
    private final ImportInvoiceRepository importInvoiceRepository;
    private final ImportInvoiceMapper importInvoiceMapper;
    private final ImportInvoiceDetailMapper importInvoiceDetailMapper;
    private final QueryGateway queryGateway;


    @QueryHandler
    public ImportInvoicePageResult handle(GetImportInvoicePageQuery getImportInvoicePageQuery, QueryMessage<?, ?> queryMessage){
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getImportInvoicePageQuery.getFilterParams();
        filterParams.put("warehouseId", warehouseId.toString());
        Pageable pageable = PaginationUtils.getPageable(getImportInvoicePageQuery.getPaginationParams(), getImportInvoicePageQuery.getSortParams());
        Specification<ImportInvoice> importInvoiceSpec = getImportInvoiceSpec(filterParams);
        Page<ImportInvoice> importInvoicePage = importInvoiceRepository.findAll(importInvoiceSpec, pageable);
        GetWarehouseInfoByIdQuery getWarehouseInfoByIdQuery = GetWarehouseInfoByIdQuery.builder()
                .warehouseId(warehouseId)
                .build();
        WarehouseInfo warehouseInfo = queryGateway.query(getWarehouseInfoByIdQuery, ResponseTypes.instanceOf(WarehouseInfo.class)).join();
        return ImportInvoicePageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(importInvoicePage))
                .content(importInvoicePage.getContent().stream()
                        .map(importInvoice -> {
                            ImportInvoiceDTO importInvoiceDTO = importInvoiceMapper.toDTO(importInvoice);
                            importInvoiceDTO.setWarehouse(warehouseInfo);
                            GetUserInfoByIdQuery getUserInfoByIdQuery = GetUserInfoByIdQuery.builder()
                                    .userId(importInvoice.getUserId())
                                    .build();
                            UserInfo userInfo = queryGateway.query(getUserInfoByIdQuery, ResponseTypes.instanceOf(UserInfo.class)).join();
                            importInvoiceDTO.setUser(userInfo);
                            List<ImportInvoiceDetailDTO> importInvoiceDetails = importInvoice.getImportInvoiceDetails().stream()
                                    .map(importInvoiceDetail -> {
                                        ImportInvoiceDetailDTO importInvoiceDetailDTO = importInvoiceDetailMapper.toDTO(importInvoiceDetail);
                                        GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                                                .productId(importInvoiceDetail.getProductId())
                                                .build();
                                        ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery, ResponseTypes.instanceOf(ProductInfo.class)).join();
                                        importInvoiceDetailDTO.setProduct(productInfo);
                                        ProductUnitInfo productUnitInfo = productInfo.getProductUnits().stream()
                                                .filter(productUnit -> productUnit.getProductUnitId().equals(importInvoiceDetail.getProductUnitId()))
                                                .findFirst()
                                                .orElse(null);
                                        importInvoiceDetailDTO.setProductUnit(productUnitInfo);
                                        return importInvoiceDetailDTO;
                                    })
                                    .toList();
                            importInvoiceDTO.setImportInvoiceDetails(importInvoiceDetails);
                            return importInvoiceDTO;
                        })
                        .toList())
                .build();
    }

    private Specification<ImportInvoice> getImportInvoiceSpec(Map<String, String> filterParams) {
        Specification<ImportInvoice> importInvoiceSpec = Specification.where(null);
        importInvoiceSpec = importInvoiceSpec.and(SpecificationUtils.buildSpecification(filterParams, "warehouseId", Long.class));
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query");
            Specification<ImportInvoice> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("importInvoiceId"), likePattern),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("supplier").get("supplierName"))),
                                likePattern)
                );
            };
            importInvoiceSpec = importInvoiceSpec.and(querySpec);
        }
        return importInvoiceSpec;
    }

    @QueryHandler
    public Long handle(GetImportInvoiceCountQuery getImportInvoiceCountQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        return importInvoiceRepository.countByRange(warehouseId, getImportInvoiceCountQuery.getStartDate(), getImportInvoiceCountQuery.getEndDate());
    }
}
