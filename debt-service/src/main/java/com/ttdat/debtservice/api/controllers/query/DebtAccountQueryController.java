package com.ttdat.debtservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.infrastructure.utils.RequestParamsUtils;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccountPageResult;
import com.ttdat.debtservice.application.queries.debtaccount.GetPartyDebtAccountPageQuery;
import com.ttdat.debtservice.application.queries.debtaccount.GetUnpaidDebtAccountByPartyIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/debt-accounts")
@RequiredArgsConstructor
public class DebtAccountQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/party/{partyId}/unpaid")
    public ApiResponse<List<PartyDebtAccount>> getUnpaidDebtAccountByPartyId(@PathVariable String partyId) {
        GetUnpaidDebtAccountByPartyIdQuery getUnpaidDebtAccountByPartyIdQuery = GetUnpaidDebtAccountByPartyIdQuery.builder()
                .partyId(partyId)
                .build();
        List<PartyDebtAccount> partyDebtAccounts = queryGateway.query(getUnpaidDebtAccountByPartyIdQuery, ResponseTypes.multipleInstancesOf(PartyDebtAccount.class)).join();
        return ApiResponse.<List<PartyDebtAccount>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Debt account retrieved successfully")
                .payload(partyDebtAccounts)
                .build();
    }

    @GetMapping("/party/{partyId}/page")
    public ApiResponse<PartyDebtAccountPageResult> getPartyDebtAccountPage(@PathVariable String partyId, @RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPartyDebtAccountPageQuery getPartyDebtAccountPageQuery = GetPartyDebtAccountPageQuery.builder()
                .partyId(partyId)
                .sortParams(sortParams)
                .paginationParams(paginationParams)
                .filterParams(filterParams)
                .build();
        PartyDebtAccountPageResult partyDebtAccountPageResult = queryGateway.query(getPartyDebtAccountPageQuery, ResponseTypes.instanceOf(PartyDebtAccountPageResult.class)).join();
        return ApiResponse.<PartyDebtAccountPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Debt account page retrieved successfully")
                .payload(partyDebtAccountPageResult)
                .build();
    }

}
