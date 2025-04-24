package com.ttdat.salesservice.application.handlers.query;

import com.ttdat.core.api.dto.response.*;
import com.ttdat.core.application.queries.customer.GetCustomerInfoByIdQuery;
import com.ttdat.core.application.queries.customer.SearchCustomerIdListQuery;
import com.ttdat.core.application.queries.exportinvoice.GetExportSummaryInRangeQuery;
import com.ttdat.core.application.queries.exportinvoice.SearchExportInvoiceIdListQuery;
import com.ttdat.core.application.queries.exportinvoice.SearchWarehouseExportInvoiceIdListQuery;
import com.ttdat.core.application.queries.inventory.GetProductBatchInfoByIdQuery;
import com.ttdat.core.application.queries.inventory.GetWarehouseInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductStockQuantityQuery;
import com.ttdat.core.application.queries.stats.GetExportInvoiceCountQuery;
import com.ttdat.core.application.queries.user.GetUserInfoByIdQuery;
import com.ttdat.core.infrastructure.utils.DateUtils;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDTO;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDetailBatchDTO;
import com.ttdat.salesservice.api.dto.common.ExportInvoiceDetailDTO;
import com.ttdat.salesservice.api.dto.response.ExportInvoicePageResult;
import com.ttdat.salesservice.api.dto.response.TopSellingProductChartData;
import com.ttdat.salesservice.application.mappers.ExportInvoiceDetailBatchMapper;
import com.ttdat.salesservice.application.mappers.ExportInvoiceDetailMapper;
import com.ttdat.salesservice.application.mappers.ExportInvoiceMapper;
import com.ttdat.salesservice.application.queries.exportinvoice.GetExportInvoicePageQuery;
import com.ttdat.salesservice.application.queries.exportinvoice.GetTopSellingProductQuery;
import com.ttdat.salesservice.application.queries.exportinvoice.GetTotalExportInRangeQuery;
import com.ttdat.salesservice.domain.entities.ExportInvoice;
import com.ttdat.salesservice.domain.repositories.ExportInvoiceRepository;
import com.ttdat.salesservice.infrastructure.utils.PaginationUtils;
import com.ttdat.salesservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
                            GetCustomerInfoByIdQuery getCustomerInfoByIdQuery = GetCustomerInfoByIdQuery.builder()
                                    .customerId(exportInvoice.getCustomerId())
                                    .build();
                            CustomerInfo customerInfo = queryGateway.query(getCustomerInfoByIdQuery, ResponseTypes.instanceOf(CustomerInfo.class)).join();
                            exportInvoiceDTO.setCustomer(customerInfo);
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
                querySpec = querySpec.or(customerNameSpec);
            }
            importInvoiceSpec = importInvoiceSpec.and(querySpec);
        }
        return importInvoiceSpec;
    }

    @QueryHandler
    public Long handle(GetExportInvoiceCountQuery getExportInvoiceCountQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        return exportInvoiceRepository.countByRange(warehouseId, getExportInvoiceCountQuery.getStartDate(), getExportInvoiceCountQuery.getEndDate());
    }

    @QueryHandler
    public List<String> handle(SearchWarehouseExportInvoiceIdListQuery searchWarehouseExportInvoiceIdListQuery){
        Long warehouseId = searchWarehouseExportInvoiceIdListQuery.getWarehouseId();
        Map<String, String> filterParams = Map.of("warehouseId", warehouseId.toString());
        Specification<ExportInvoice> exportInvoiceSpec = getExportInvoiceSpec(filterParams);
        List<ExportInvoice> exportInvoices = exportInvoiceRepository.findAll(exportInvoiceSpec);
        return exportInvoices.stream()
                .map(ExportInvoice::getExportInvoiceId)
                .toList();
    }

    @QueryHandler
    public List<String> handle(SearchExportInvoiceIdListQuery searchExportInvoiceIdListQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        return exportInvoiceRepository.findByRange(warehouseId, searchExportInvoiceIdListQuery.getStartDate(), searchExportInvoiceIdListQuery.getEndDate()).stream()
                .map(ExportInvoice::getExportInvoiceId)
                .toList();
    }

    @QueryHandler
    public BigDecimal handle(GetTotalExportInRangeQuery getTotalExportInRangeQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        List<ExportInvoice> exportInvoices = exportInvoiceRepository.findByRange(warehouseId, getTotalExportInRangeQuery.getStartDate(), getTotalExportInRangeQuery.getEndDate());
        return exportInvoices.stream()
                .map(exportInvoice -> exportInvoice.getExportInvoiceDetails().stream()
                        .map(exportInvoiceDetail -> {
                            GetProductStockQuantityQuery getProductStockQuantityQuery = GetProductStockQuantityQuery.builder()
                                    .productId(exportInvoiceDetail.getProductId())
                                    .productUnitId(exportInvoiceDetail.getProductUnitId())
                                    .quantity(Double.valueOf(exportInvoiceDetail.getQuantity()))
                                    .build();
                            Double stockQuantity = queryGateway.query(getProductStockQuantityQuery, ResponseTypes.instanceOf(Double.class)).join();
                            return BigDecimal.valueOf(stockQuantity);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private Specification<ExportInvoice> getExportInvoiceStatsSpec(Map<String, String> filterParams) {
        Specification<ExportInvoice> exportInvoiceStatsSpec = Specification.where(null);
        exportInvoiceStatsSpec = exportInvoiceStatsSpec.and(SpecificationUtils.buildSpecification(filterParams, "warehouseId", Long.class));
        if (filterParams.containsKey("startDate") && filterParams.containsKey("type") && !filterParams.containsKey("endDate")) {
            String startDateStr = filterParams.get("startDate");
            String type = filterParams.get("type");
            LocalDate startDate = DateUtils.parseDate(startDateStr, type);
            exportInvoiceStatsSpec = exportInvoiceStatsSpec.and((root, query, cb) -> {
                LocalDate endDate;
                if (type.equalsIgnoreCase("date")) {
                    endDate = startDate.plusDays(1);
                } else if (type.equalsIgnoreCase("month")) {
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()).plusDays(1);
                } else if (type.equalsIgnoreCase("quarter")) {
                    endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1).plusDays(1);
                } else if (type.equalsIgnoreCase("year")) {
                    endDate = startDate.withDayOfYear(startDate.lengthOfYear()).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Invalid date type: " + type);
                }
                return cb.between(root.get("createdDate"), startDate, endDate);
            });
        }

        if (filterParams.containsKey("startDate") && filterParams.containsKey("endDate") && filterParams.get("type").equalsIgnoreCase("date")) {
            String startDateStr = filterParams.get("startDate");
            LocalDate startDate = DateUtils.parseDate(startDateStr, "date");
            String endDateStr = filterParams.get("endDate");
            LocalDate endDate = DateUtils.parseDate(endDateStr, "date");
            exportInvoiceStatsSpec = exportInvoiceStatsSpec.and((root, query, cb) -> cb.between(root.get("createdDate"), startDate, endDate));
        }
        return exportInvoiceStatsSpec;
    }

    @QueryHandler
    public List<ExportSummary> handle(GetExportSummaryInRangeQuery getExportSummaryInRangeQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = new HashMap<>();
        filterParams.put("warehouseId", warehouseId.toString());
        filterParams.put("startDate", getExportSummaryInRangeQuery.getStartDateStr());
        filterParams.put("endDate", getExportSummaryInRangeQuery.getEndDateStr());
        filterParams.put("type", getExportSummaryInRangeQuery.getType());
        Specification<ExportInvoice> exportInvoiceSpec = getExportInvoiceStatsSpec(filterParams);
        List<ExportInvoice> exportInvoices = exportInvoiceRepository.findAll(exportInvoiceSpec);
        return exportInvoices.stream()
                .map(exportInvoice -> {
                    BigDecimal totalQuantity = exportInvoice.getExportInvoiceDetails().stream()
                            .map(exportInvoiceDetail -> {
                                GetProductStockQuantityQuery getProductStockQuantityQuery = GetProductStockQuantityQuery.builder()
                                        .productId(exportInvoiceDetail.getProductId())
                                        .productUnitId(exportInvoiceDetail.getProductUnitId())
                                        .quantity(Double.valueOf(exportInvoiceDetail.getQuantity()))
                                        .build();
                                Double stockQuantity = queryGateway.query(getProductStockQuantityQuery, ResponseTypes.instanceOf(Double.class)).join();
                                return BigDecimal.valueOf(stockQuantity);
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return ExportSummary.builder()
                            .exportInvoiceId(exportInvoice.getExportInvoiceId())
                            .createdDate(exportInvoice.getCreatedDate())
                            .totalQuantity(totalQuantity)
                            .build();
                })
                .toList();
    }

    @QueryHandler
    public List<TopSellingProductChartData> handle(GetTopSellingProductQuery getTopSellingProductQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");

        List<ExportInvoice> exportInvoices = exportInvoiceRepository.findByWarehouseId(warehouseId);
        Map<String, Double> productSellingMap = new HashMap<>();

        // Count occurrences of each product in export invoice details
        exportInvoices.forEach(invoice ->
            invoice.getExportInvoiceDetails().forEach(detail -> {
                String productId = detail.getProductId();
                productSellingMap.merge(productId, 1.0, Double::sum);
            })
        );

        // Get top 5 products and their info
        return productSellingMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                GetProductInfoByIdQuery getProductInfoByIdQuery = GetProductInfoByIdQuery.builder()
                    .productId(entry.getKey())
                    .build();
                ProductInfo productInfo = queryGateway.query(getProductInfoByIdQuery,
                    ResponseTypes.instanceOf(ProductInfo.class)).join();

                return TopSellingProductChartData.builder()
                    .productName(productInfo.getProductName())
                    .totalSales(entry.getValue())
                    .build();
            })
            .toList();
    }
}
