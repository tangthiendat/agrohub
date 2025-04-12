package com.ttdat.customerservice.application.handlers.query;

import com.ttdat.core.api.dto.response.CustomerInfo;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.core.application.queries.customer.GetCustomerInfoByIdQuery;
import com.ttdat.core.application.queries.customer.SearchCustomerIdListQuery;
import com.ttdat.customerservice.api.dto.CustomerDTO;
import com.ttdat.customerservice.api.dto.response.CustomerPageResult;
import com.ttdat.customerservice.application.mappers.CustomerMapper;
import com.ttdat.customerservice.application.queries.customer.GetCustomerQueryPageQuery;
import com.ttdat.customerservice.application.queries.customer.SearchCustomerQuery;
import com.ttdat.customerservice.application.repositories.CustomerRepository;
import com.ttdat.customerservice.domain.entities.Customer;
import com.ttdat.customerservice.infrastructure.utils.PaginationUtils;
import com.ttdat.customerservice.infrastructure.utils.SpecificationUtils;
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
public class CustomerQueryHandler {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @QueryHandler
    public CustomerPageResult handle(GetCustomerQueryPageQuery getCustomerQueryPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getCustomerQueryPageQuery.getPaginationParams(), getCustomerQueryPageQuery.getSortParams());
        Specification<Customer> customerSpec = getCustomerSpec(getCustomerQueryPageQuery.getFilterParams());
        Page<Customer> customerPage = customerRepository.findAll(customerSpec, pageable);
        return CustomerPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(customerPage))
                .content(customerMapper.toDTOList(customerPage.getContent()))
                .build();
    }

    private Specification<Customer> getCustomerSpec(Map<String, String> filterParams) {
        Specification<Customer> customerSpec = Specification.where(null);
        customerSpec = customerSpec.and(SpecificationUtils.buildSpecification(filterParams, "active", Boolean.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "customerType", String.class));
        if (filterParams.containsKey("query")) {
            String searchValue = filterParams.get("query").toLowerCase();
            Specification<Customer> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("customerId"), likePattern),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("customerName"))),
                                likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), likePattern)
                );
            };
            customerSpec = customerSpec.and(querySpec);
        }
        return customerSpec;
    }

    @QueryHandler
    public List<CustomerDTO> handle(SearchCustomerQuery searchCustomerQuery) {
        Specification<Customer> customerSpec = getCustomerSpec(Map.of("query", searchCustomerQuery.getQuery()));
        List<Customer> customers = customerRepository.findAll(customerSpec);
        return customerMapper.toDTOList(customers);
    }

    @QueryHandler
    public CustomerInfo handle(GetCustomerInfoByIdQuery getCustomerInfoByIdQuery) {
        Customer customer = customerRepository.findById(getCustomerInfoByIdQuery.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
        return customerMapper.toCustomerInfo(customer);
    }

    @QueryHandler
    public List<String> handle(SearchCustomerIdListQuery searchCustomerIdListQuery){
        Specification<Customer> customerSpec = getCustomerSpec(searchCustomerIdListQuery.getFilterParams());
        List<Customer> customers = customerRepository.findAll(customerSpec);
        return customers.stream().map(Customer::getCustomerId).toList();
    }
}
