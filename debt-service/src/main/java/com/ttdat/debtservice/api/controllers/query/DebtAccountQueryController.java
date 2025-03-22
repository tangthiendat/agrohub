package com.ttdat.debtservice.api.controllers.query;

import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.debtservice.api.dto.response.PartyDebtAccount;
import com.ttdat.debtservice.application.queries.debtaccount.GetUnpaidDebtAccountByPartyIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/debt-accounts")
@RequiredArgsConstructor
public class DebtAccountQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/unpaid/party/{partyId}")
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

}
