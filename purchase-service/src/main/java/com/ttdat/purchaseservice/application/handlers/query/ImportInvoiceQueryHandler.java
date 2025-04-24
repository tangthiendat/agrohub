package com.ttdat.purchaseservice.application.handlers.query;

import com.ttdat.core.api.dto.response.*;
import com.ttdat.core.application.queries.importinvoice.GetImportSummaryInRangeQuery;
import com.ttdat.core.application.queries.inventory.GetWarehouseInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductStockQuantityQuery;
import com.ttdat.core.application.queries.stats.GetImportInvoiceCountQuery;
import com.ttdat.core.application.queries.user.GetUserInfoByIdQuery;
import com.ttdat.core.infrastructure.utils.DateUtils;
import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDTO;
import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDetailDTO;
import com.ttdat.purchaseservice.api.dto.response.ImportInvoicePageResult;
import com.ttdat.purchaseservice.application.mappers.ImportInvoiceDetailMapper;
import com.ttdat.purchaseservice.application.mappers.ImportInvoiceMapper;
import com.ttdat.purchaseservice.application.queries.importinvoice.GetImportInvoicePageQuery;
import com.ttdat.purchaseservice.application.queries.importinvoice.GetTotalImportInRangeQuery;
import com.ttdat.purchaseservice.domain.entities.ImportInvoice;
import com.ttdat.purchaseservice.domain.repositories.ImportInvoiceRepository;
import com.ttdat.purchaseservice.infrastructure.utils.PaginationUtils;
import com.ttdat.purchaseservice.infrastructure.utils.SpecificationUtils;
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

    @QueryHandler
    public BigDecimal handle(GetTotalImportInRangeQuery getTotalImportInRangeQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        List<ImportInvoice> importInvoices = importInvoiceRepository.findByRange(warehouseId, getTotalImportInRangeQuery.getStartDate(), getTotalImportInRangeQuery.getEndDate());
        return importInvoices.stream()
                .map(importInvoice -> importInvoice.getImportInvoiceDetails().stream()
                        .map(importInvoiceDetail -> {
                            GetProductStockQuantityQuery getProductStockQuantityQuery = GetProductStockQuantityQuery.builder()
                                    .productId(importInvoiceDetail.getProductId())
                                    .productUnitId(importInvoiceDetail.getProductUnitId())
                                    .quantity(Double.valueOf(importInvoiceDetail.getQuantity()))
                                    .build();
                            Double stockQuantity = queryGateway.query(getProductStockQuantityQuery, ResponseTypes.instanceOf(Double.class)).join();
                            return BigDecimal.valueOf(stockQuantity);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Specification<ImportInvoice> getImportInvoiceStatsSpec(Map<String, String> filterParams) {
        Specification<ImportInvoice> importInvoiceStatsSpec = Specification.where(null);
        importInvoiceStatsSpec = importInvoiceStatsSpec.and(SpecificationUtils.buildSpecification(filterParams, "warehouseId", Long.class));

        if (filterParams.containsKey("startDate") && filterParams.containsKey("type") && !filterParams.containsKey("endDate")) {
            String startDateStr = filterParams.get("startDate");
            String type = filterParams.get("type");
            log.info("Start date: {}, Type: {}", startDateStr, type);
            LocalDate startDate = DateUtils.parseDate(startDateStr, type);
            log.info("Parsed start date: {}", startDate);
            importInvoiceStatsSpec = importInvoiceStatsSpec.and((root, query, cb) -> {
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
            importInvoiceStatsSpec = importInvoiceStatsSpec.and((root, query, cb) -> cb.between(root.get("createdDate"), startDate, endDate));
        }
        return importInvoiceStatsSpec;
    }

    @QueryHandler
    public List<ImportSummary> handle(GetImportSummaryInRangeQuery getImportSummaryInRangeQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Map<String, String> filterParams = new HashMap<>();
        filterParams.put("warehouseId", warehouseId.toString());
        filterParams.put("startDate", getImportSummaryInRangeQuery.getStartDateStr());
        filterParams.put("endDate", getImportSummaryInRangeQuery.getEndDateStr());
        filterParams.put("type", getImportSummaryInRangeQuery.getType());
        Specification<ImportInvoice> importInvoiceSpec = getImportInvoiceStatsSpec(filterParams);
        List<ImportInvoice> importInvoices = importInvoiceRepository.findAll(importInvoiceSpec);
        return importInvoices.stream()
                .map(importInvoice -> {
                   BigDecimal totalQuantity = importInvoice.getImportInvoiceDetails().stream()
                            .map(importInvoiceDetail -> {
                                GetProductStockQuantityQuery getProductStockQuantityQuery = GetProductStockQuantityQuery.builder()
                                        .productId(importInvoiceDetail.getProductId())
                                        .productUnitId(importInvoiceDetail.getProductUnitId())
                                        .quantity(Double.valueOf(importInvoiceDetail.getQuantity()))
                                        .build();
                                Double stockQuantity = queryGateway.query(getProductStockQuantityQuery, ResponseTypes.instanceOf(Double.class)).join();
                                return BigDecimal.valueOf(stockQuantity);
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return ImportSummary.builder()
                            .importInvoiceId(importInvoice.getImportInvoiceId())
                            .createdDate(importInvoice.getCreatedDate())
                            .totalQuantity(totalQuantity)
                            .build();
                })
                .toList();
    }
}
