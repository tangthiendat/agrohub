package com.ttdat.debtservice.application.handlers.query;

import com.ttdat.core.api.dto.response.CustomerInfo;
import com.ttdat.core.application.queries.customer.GetCustomerInfoByIdQuery;
import com.ttdat.core.application.queries.exportinvoice.SearchExportInvoiceIdListQuery;
import com.ttdat.core.application.queries.exportinvoice.SearchWarehouseExportInvoiceIdListQuery;
import com.ttdat.core.domain.entities.DebtPartyType;
import com.ttdat.core.domain.entities.DebtStatus;
import com.ttdat.debtservice.api.dto.common.DebtAccountDTO;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccountPageResult;
import com.ttdat.debtservice.api.dto.response.TopCustomerDebtChartData;
import com.ttdat.debtservice.application.mappers.DebtAccountMapper;
import com.ttdat.debtservice.application.queries.debtaccount.GetPartyDebtAccountPageQuery;
import com.ttdat.debtservice.application.queries.debtaccount.GetTopCustomerDebtQuery;
import com.ttdat.debtservice.application.queries.debtaccount.GetTotalCustomerDebtInRangeQuery;
import com.ttdat.debtservice.application.queries.debtaccount.GetUnpaidDebtAccountByPartyIdQuery;
import com.ttdat.debtservice.domain.repositories.DebtAccountRepository;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import com.ttdat.debtservice.infrastructure.utils.PaginationUtils;
import com.ttdat.debtservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DebtAccountQueryHandler {
    private final DebtAccountRepository debtAccountRepository;
    private final DebtAccountMapper debtAccountMapper;
    private final QueryGateway queryGateway;
    private final JdbcTemplate jdbcTemplate;

    @QueryHandler
    public List<PartyDebtAccount> handle(GetUnpaidDebtAccountByPartyIdQuery getUnpaidDebtAccountByPartyIdQuery) {
        List<DebtAccount> debtAccounts = debtAccountRepository.getPartyUnpaidDebtAccounts(getUnpaidDebtAccountByPartyIdQuery.getPartyId(), getUnpaidDebtAccountByPartyIdQuery.getPartyType());
        return debtAccountMapper.toPartyDebtAccountList(debtAccounts);
    }

    @QueryHandler
    public PartyDebtAccountPageResult handle(GetPartyDebtAccountPageQuery getPartyDebtAccountPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getPartyDebtAccountPageQuery.getPaginationParams(), getPartyDebtAccountPageQuery.getSortParams());
        Map<String, String> filterParams = getPartyDebtAccountPageQuery.getFilterParams();
        filterParams.put("partyId", getPartyDebtAccountPageQuery.getPartyId());
        filterParams.put("partyType", getPartyDebtAccountPageQuery.getPartyType().toString());
        Specification<DebtAccount> debtAccountSpec = getPartyDebtAccountSpec(getPartyDebtAccountPageQuery.getFilterParams());
        Page<DebtAccount> debtAccounts = debtAccountRepository.findAll(debtAccountSpec, pageable);
        List<DebtAccountDTO> partyDebtAccounts = debtAccountMapper.toDTOList(debtAccounts.getContent());
        partyDebtAccounts.forEach(debtAccountDTO -> {
            // Sort the transactions by createdAt in descending order
            debtAccountDTO.getDebtTransactions().sort((debtTransaction1, debtTransaction2) -> {
                if (debtTransaction1.getCreatedAt() == null || debtTransaction2.getCreatedAt() == null) {
                    return 0;
                }
                return debtTransaction2.getCreatedAt().compareTo(debtTransaction1.getCreatedAt());
            });
        });
        return PartyDebtAccountPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(debtAccounts))
                .content(partyDebtAccounts)
                .build();
    }

    private Specification<DebtAccount> getPartyDebtAccountSpec(Map<String, String> filterParams) {
        Specification<DebtAccount> spec = Specification.where(null);
        spec = spec.and(SpecificationUtils.buildSpecification(filterParams, "partyId", String.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "partyType", String.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "debtStatus", String.class));
        return spec;
    }

    @QueryHandler
    public BigDecimal handle(GetTotalCustomerDebtInRangeQuery getTotalCustomerDebtInRangeQuery) {
        Map<String, String> debtFilterParams = new HashMap<>();
        debtFilterParams.put("partyType", DebtPartyType.CUSTOMER.toString());
        debtFilterParams.put("debtStatus", DebtStatus.UNPAID.toString());
        Specification<DebtAccount> debtAccountSpec = getPartyDebtAccountSpec(debtFilterParams);
        LocalDate startDate = getTotalCustomerDebtInRangeQuery.getStartDate();
        LocalDate endDate = getTotalCustomerDebtInRangeQuery.getEndDate();
        if (startDate != null && endDate != null) {
            SearchExportInvoiceIdListQuery searchExportInvoiceIdListQuery = SearchExportInvoiceIdListQuery.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            List<String> exportInvoiceIds = queryGateway.query(searchExportInvoiceIdListQuery, ResponseTypes.multipleInstancesOf(String.class)).join();
            if (!exportInvoiceIds.isEmpty()) {
                Specification<DebtAccount> exportInvoiceSpec = (root, query, criteriaBuilder) ->
                        root.get("sourceId").in(exportInvoiceIds);
                debtAccountSpec = debtAccountSpec.and(exportInvoiceSpec);
            }
        }
        List<DebtAccount> customerDebtAccounts = debtAccountRepository.findAll(debtAccountSpec);
        return customerDebtAccounts.stream()
                .map(DebtAccount::getRemainingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @QueryHandler
    public List<TopCustomerDebtChartData> handle(GetTopCustomerDebtQuery getTopCustomerDebtQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        SearchWarehouseExportInvoiceIdListQuery searchWarehouseExportInvoiceIdListQuery = SearchWarehouseExportInvoiceIdListQuery.builder()
                .warehouseId(warehouseId)
                .build();
        List<String> exportInvoiceIdList = queryGateway.query(searchWarehouseExportInvoiceIdListQuery, ResponseTypes.multipleInstancesOf(String.class)).join();

        List<DebtAccount> debtAccounts = debtAccountRepository.getAllUnpaidDebtAccountByParty(DebtPartyType.CUSTOMER, exportInvoiceIdList);

        Map<String, BigDecimal> customerDebtAccountMap = debtAccounts.stream()
                .collect(HashMap::new, (map, debtAccount) -> {
                    String customerId = debtAccount.getPartyId();
                    GetCustomerInfoByIdQuery getCustomerInfoByIdQuery = GetCustomerInfoByIdQuery.builder()
                            .customerId(customerId)
                            .build();
                    CustomerInfo customerInfo = queryGateway.query(getCustomerInfoByIdQuery, ResponseTypes.instanceOf(CustomerInfo.class)).join();
                    BigDecimal remainingAmount = debtAccount.getRemainingAmount();
                    map.merge(customerInfo.getCustomerName(), remainingAmount, BigDecimal::add);
                }, HashMap::putAll);
        return customerDebtAccountMap.entrySet().stream()
                .map(entry -> {
                    String customerName = entry.getKey();
                    BigDecimal totalDebt = entry.getValue();
                    return TopCustomerDebtChartData.builder()
                            .customerName(customerName)
                            .totalDebt(totalDebt)
                            .build();
                })
                .sorted((o1, o2) -> {
                    if (o1.getTotalDebt() == null || o2.getTotalDebt() == null) {
                        return 0;
                    }
                    return o2.getTotalDebt().compareTo(o1.getTotalDebt());
                })
                .limit(5)
                .toList();

    }

}
