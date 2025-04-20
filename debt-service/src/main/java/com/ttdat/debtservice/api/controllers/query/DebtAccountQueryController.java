package com.ttdat.debtservice.api.controllers.query;

import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import com.ttdat.core.api.dto.response.ApiResponse;
import com.ttdat.core.domain.entities.DebtPartyType;
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

    @GetMapping("/supplier/{supplierId}/unpaid")
    public ApiResponse<List<PartyDebtAccount>> getUnpaidDebtAccountBySupplierId(@PathVariable String supplierId) {
        GetUnpaidDebtAccountByPartyIdQuery getUnpaidDebtAccountByPartyIdQuery = GetUnpaidDebtAccountByPartyIdQuery.builder()
                .partyId(supplierId)
                .partyType(DebtPartyType.SUPPLIER)
                .build();
        List<PartyDebtAccount> partyDebtAccounts = queryGateway.query(getUnpaidDebtAccountByPartyIdQuery, ResponseTypes.multipleInstancesOf(PartyDebtAccount.class)).join();
        return ApiResponse.<List<PartyDebtAccount>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Supplier unpaid debt account retrieved successfully")
                .payload(partyDebtAccounts)
                .build();
    }

    @GetMapping("/customer/{customerId}/unpaid")
    public ApiResponse<List<PartyDebtAccount>> getUnpaidDebtAccountByCustomerId(@PathVariable String customerId) {
        GetUnpaidDebtAccountByPartyIdQuery getUnpaidDebtAccountByPartyIdQuery = GetUnpaidDebtAccountByPartyIdQuery.builder()
                .partyId(customerId)
                .partyType(DebtPartyType.CUSTOMER)
                .build();
        List<PartyDebtAccount> partyDebtAccounts = queryGateway.query(getUnpaidDebtAccountByPartyIdQuery, ResponseTypes.multipleInstancesOf(PartyDebtAccount.class)).join();
        return ApiResponse.<List<PartyDebtAccount>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Customer unpaid debt account retrieved successfully")
                .payload(partyDebtAccounts)
                .build();
    }

    @GetMapping("/supplier/{supplierId}/page")
    public ApiResponse<PartyDebtAccountPageResult> getSupplierDebtAccountPage(@PathVariable String supplierId, @RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPartyDebtAccountPageQuery getPartyDebtAccountPageQuery = GetPartyDebtAccountPageQuery.builder()
                .partyId(supplierId)
                .partyType(DebtPartyType.SUPPLIER)
                .sortParams(sortParams)
                .paginationParams(paginationParams)
                .filterParams(filterParams)
                .build();
        PartyDebtAccountPageResult partyDebtAccountPageResult = queryGateway.query(getPartyDebtAccountPageQuery, ResponseTypes.instanceOf(PartyDebtAccountPageResult.class)).join();
        return ApiResponse.<PartyDebtAccountPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Supplier debt account page retrieved successfully")
                .payload(partyDebtAccountPageResult)
                .build();
    }

    @GetMapping("/customer/{customerId}/page")
    public ApiResponse<PartyDebtAccountPageResult> getCustomerDebtAccountPage(@PathVariable String customerId, @RequestParam Map<String, String> filterParams) {
        PaginationParams paginationParams = RequestParamsUtils.getPaginationParams(filterParams);
        SortParams sortParams = RequestParamsUtils.getSortParams(filterParams);
        GetPartyDebtAccountPageQuery getPartyDebtAccountPageQuery = GetPartyDebtAccountPageQuery.builder()
                .partyId(customerId)
                .partyType(DebtPartyType.CUSTOMER)
                .sortParams(sortParams)
                .paginationParams(paginationParams)
                .filterParams(filterParams)
                .build();
        PartyDebtAccountPageResult partyDebtAccountPageResult = queryGateway.query(getPartyDebtAccountPageQuery, ResponseTypes.instanceOf(PartyDebtAccountPageResult.class)).join();
        return ApiResponse.<PartyDebtAccountPageResult>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Customer debt account page retrieved successfully")
                .payload(partyDebtAccountPageResult)
                .build();
    }

}
