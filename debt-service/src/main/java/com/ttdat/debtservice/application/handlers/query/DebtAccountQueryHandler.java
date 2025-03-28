package com.ttdat.debtservice.application.handlers.query;

import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccountPageResult;
import com.ttdat.debtservice.application.mappers.DebtAccountMapper;
import com.ttdat.debtservice.application.queries.debtaccount.GetPartyDebtAccountPageQuery;
import com.ttdat.debtservice.application.queries.debtaccount.GetUnpaidDebtAccountByPartyIdQuery;
import com.ttdat.debtservice.application.repositories.DebtAccountRepository;
import com.ttdat.debtservice.domain.entities.DebtAccount;
import com.ttdat.debtservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DebtAccountQueryHandler {
    private final DebtAccountRepository debtAccountRepository;
    private final DebtAccountMapper debtAccountMapper;

    @QueryHandler
    public List<PartyDebtAccount> handle(GetUnpaidDebtAccountByPartyIdQuery getUnpaidDebtAccountByPartyIdQuery) {
        List<DebtAccount> debtAccounts = debtAccountRepository.getPartyDebtAccounts(getUnpaidDebtAccountByPartyIdQuery.getPartyId());
        return debtAccountMapper.toPartyDebtAccountList(debtAccounts);
    }

    @QueryHandler
    public PartyDebtAccountPageResult handle(GetPartyDebtAccountPageQuery getPartyDebtAccountPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getPartyDebtAccountPageQuery.getPaginationParams(), getPartyDebtAccountPageQuery.getSortParams());
        Page<DebtAccount> debtAccounts = debtAccountRepository.findByPartyId(getPartyDebtAccountPageQuery.getPartyId(), pageable);
        return PartyDebtAccountPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(debtAccounts))
                .content(debtAccountMapper.toDTOList(debtAccounts.getContent()))
                .build();
    }

}
