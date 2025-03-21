package com.ttdat.debtservice.application.handlers.query;

import com.ttdat.debtservice.api.dto.common.PaymentMethodDTO;
import com.ttdat.debtservice.api.dto.response.PaymentMethodPageResult;
import com.ttdat.debtservice.application.mappers.PaymentMethodMapper;
import com.ttdat.debtservice.application.queries.paymentmethod.GetAllPaymentMethodQuery;
import com.ttdat.debtservice.application.queries.paymentmethod.GetPaymentMethodPageQuery;
import com.ttdat.debtservice.application.repositories.PaymentMethodRepository;
import com.ttdat.debtservice.domain.entities.PaymentMethod;
import com.ttdat.debtservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentMethodQueryHandler {
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @QueryHandler
    public PaymentMethodPageResult handle(GetPaymentMethodPageQuery getPaymentMethodPageQuery){
        Pageable pageable = PaginationUtils.getPageable(getPaymentMethodPageQuery.getPaginationParams(), getPaymentMethodPageQuery.getSortParams());
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findAll(pageable);
        return PaymentMethodPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(paymentMethods))
                .content(paymentMethodMapper.toDTOList(paymentMethods.getContent()))
                .build();
    }

    @QueryHandler
    public List<PaymentMethodDTO> handle(GetAllPaymentMethodQuery getAllPaymentMethodQuery){
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return paymentMethodMapper.toDTOList(paymentMethods);
    }
}
