package com.ttdat.customerservice.application.handlers.query;

import com.ttdat.customerservice.api.dto.CustomerPageResult;
import com.ttdat.customerservice.application.mappers.CustomerMapper;
import com.ttdat.customerservice.application.queries.customer.GetCustomerQueryPageQuery;
import com.ttdat.customerservice.application.repositories.CustomerRepository;
import com.ttdat.customerservice.domain.entities.Customer;
import com.ttdat.customerservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerQueryHandler {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @QueryHandler
    public CustomerPageResult handle(GetCustomerQueryPageQuery getCustomerQueryPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getCustomerQueryPageQuery.getPaginationParams(), getCustomerQueryPageQuery.getSortParams());
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return CustomerPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(customerPage))
                .content(customerMapper.toDTOList(customerPage.getContent()))
                .build();
    }
}
