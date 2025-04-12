package com.ttdat.salesservice.application.handlers.query;

import com.ttdat.core.api.dto.response.*;
import com.ttdat.core.application.queries.customer.SearchCustomerIdListQuery;
import com.ttdat.core.application.queries.inventory.GetProductBatchInfoByIdQuery;
import com.ttdat.core.application.queries.inventory.GetWarehouseInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.user.GetUserInfoByIdQuery;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDTO;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDetailBatchDTO;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDetailDTO;
import com.ttdat.salesservice.api.dto.response.ExportInvoicePageResult;
import com.ttdat.salesservice.application.mappers.ExportInvoiceDetailBatchMapper;
import com.ttdat.salesservice.application.mappers.ExportInvoiceDetailMapper;
import com.ttdat.salesservice.application.mappers.ExportInvoiceMapper;
import com.ttdat.salesservice.application.queries.exportinvoice.GetExportInvoicePageQuery;
import com.ttdat.salesservice.domain.entities.ExportInvoice;
import com.ttdat.salesservice.domain.repositories.ExportInvoiceRepository;
import com.ttdat.salesservice.infrastructure.utils.PaginationUtils;
import com.ttdat.salesservice.infrastructure.utils.SpecificationUtils;
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
public class ExportInvoiceQueryHandler {
    private final ExportInvoiceRepository exportInvoiceRepository;
    private final ExportInvoiceMapper exportInvoiceMapper;
    private final ExportInvoiceDetailMapper exportInvoiceDetailMapper;
    private final ExportInvoiceDetailBatchMapper exportInvoiceDetailBatchMapper;
    private final QueryGateway queryGateway;

    @QueryHandler
    public ExportInvoicePageResult handle(GetExportInvoicePageQuery getExportInvoicePageQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = getExportInvoicePageQuery.getFilterParams();
        filterParams.put("warehouseId", warehouseId.toString());
        Pageable pageable = PaginationUtils.getPageable(getExportInvoicePageQuery.getPaginationParams(), getExportInvoicePageQuery.getSortParams());
        Specification<ExportInvoice> exportInvoiceSpec = getExportInvoiceSpec(filterParams);
        Page<ExportInvoice> exportInvoicePage = exportInvoiceRepository.findAll(exportInvoiceSpec, pageable);
        GetWarehouseInfoByIdQuery getWarehouseInfoByIdQuery = GetWarehouseInfoByIdQuery.builder()
                .warehouseId(warehouseId)
                .build();
        WarehouseInfo warehouseInfo = queryGateway.query(getWarehouseInfoByIdQuery, ResponseTypes.instanceOf(WarehouseInfo.class)).join();
        return ExportInvoicePageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(exportInvoicePage))
                .content(exportInvoicePage.getContent().stream()
                        .map(exportInvoice -> {
                            ExportInvoiceDTO exportInvoiceDTO = exportInvoiceMapper.toDTO(exportInvoice);
                            exportInvoiceDTO.setWarehouse(warehouseInfo);
                            GetUserInfoByIdQuery getUserInfoByIdQuery = GetUserInfoByIdQuery.builder()
                                    .userId(exportInvoice.getUserId())
                                    .build();
                            UserInfo userInfo = queryGateway.query(getUserInfoByIdQuery, ResponseTypes.instanceOf(UserInfo.class)).join();
                            exportInvoiceDTO.setUser(userInfo);
                            List<ExportInvoiceDetailDTO> exportInvoiceDetails = exportInvoice.getExportInvoiceDetails().stream()
                                    .map(exportInvoiceDetail -> {
                                        ExportInvoiceDetailDTO importInvoiceDetailDTO = exportInvoiceDetailMapper.toDTO(exportInvoiceDetail);
                                        GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                                                .productId(exportInvoiceDetail.getProductId())
                                                .build();
                                        ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery, ResponseTypes.instanceOf(ProductInfo.class)).join();
                                        importInvoiceDetailDTO.setProduct(productInfo);
                                        ProductUnitInfo productUnitInfo = productInfo.getProductUnits().stream()
                                                .filter(productUnit -> productUnit.getProductUnitId().equals(exportInvoiceDetail.getProductUnitId()))
                                                .findFirst()
                                                .orElse(null);
                                        importInvoiceDetailDTO.setProductUnit(productUnitInfo);
                                        List<ExportInvoiceDetailBatchDTO> detailBatchDTOS = exportInvoiceDetail.getDetailBatches().stream()
                                                .map(exportInvoiceDetailBatch -> {
                                                    ExportInvoiceDetailBatchDTO exportInvoiceDetailBatchDTO = exportInvoiceDetailBatchMapper.toDTO(exportInvoiceDetailBatch);
                                                    GetProductBatchInfoByIdQuery getProductBatchInfoByIdQuery = GetProductBatchInfoByIdQuery.builder()
                                                            .warehouseId(exportInvoice.getWarehouseId())
                                                            .productBatchId(exportInvoiceDetailBatch.getBatchId())
                                                            .build();
                                                    ProductBatchInfo productBatchInfo = queryGateway.query(getProductBatchInfoByIdQuery, ResponseTypes.instanceOf(ProductBatchInfo.class)).join();
                                                    exportInvoiceDetailBatchDTO.setProductBatch(productBatchInfo);
                                                    return exportInvoiceDetailBatchDTO;
                                                })
                                                .toList();
                                        importInvoiceDetailDTO.setDetailBatches(detailBatchDTOS);
                                        return importInvoiceDetailDTO;
                                    })
                                    .toList();
                            exportInvoiceDTO.setExportInvoiceDetails(exportInvoiceDetails);
                            return exportInvoiceDTO;
                        })
                        .toList())
                .build();
    }

    private Specification<ExportInvoice> getExportInvoiceSpec(Map<String, String> filterParams) {
        Specification<ExportInvoice> importInvoiceSpec = Specification.where(null);
        importInvoiceSpec = importInvoiceSpec.and(SpecificationUtils.buildSpecification(filterParams, "warehouseId", Long.class));
        Map<String, String> customerFilterParams = new HashMap<>();
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query");
            Specification<ExportInvoice> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("exportInvoiceId"), likePattern)
                );
            };

            customerFilterParams.put("query", filterParams.get("query"));
            SearchCustomerIdListQuery searchCustomerIdListQuery = SearchCustomerIdListQuery.builder()
                    .filterParams(customerFilterParams)
                    .build();
            List<String> customerIdList = queryGateway.query(searchCustomerIdListQuery, ResponseTypes.multipleInstancesOf(String.class)).join();
            if (!customerIdList.isEmpty()) {
                Specification<ExportInvoice> customerNameSpec = (root, query, criteriaBuilder) ->
                        root.get("customerId").in(customerIdList);
                querySpec = querySpec.and(customerNameSpec);
            }

            importInvoiceSpec = importInvoiceSpec.and(querySpec);
        }
        return importInvoiceSpec;
    }
}
