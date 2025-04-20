package com.ttdat.debtservice.application.handlers.query;

import com.ttdat.debtservice.api.dto.common.DebtAccountDTO;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccountPageResult;
import com.ttdat.debtservice.application.mappers.DebtAccountMapper;
import com.ttdat.debtservice.application.queries.debtaccount.GetPartyDebtAccountPageQuery;
import com.ttdat.debtservice.application.queries.debtaccount.GetUnpaidDebtAccountByPartyIdQuery;
import com.ttdat.debtservice.domain.repositories.DebtAccountRepository;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import com.ttdat.debtservice.infrastructure.utils.PaginationUtils;
import com.ttdat.debtservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DebtAccountQueryHandler {
    private final DebtAccountRepository debtAccountRepository;
    private final DebtAccountMapper debtAccountMapper;

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
            debtAccountDTO.getDebtTransactions().sort( (debtTransaction1, debtTransaction2) -> {
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

}
