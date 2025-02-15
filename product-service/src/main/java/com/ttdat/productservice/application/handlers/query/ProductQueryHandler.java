package com.ttdat.productservice.application.handlers.query;

import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.api.dto.response.ProductPageResult;
import com.ttdat.productservice.application.mappers.ProductMapper;
import com.ttdat.productservice.application.queries.product.GetProductByIdQuery;
import com.ttdat.productservice.application.queries.product.GetProductPageQuery;
import com.ttdat.productservice.domain.entities.Product;
import com.ttdat.productservice.domain.repositories.ProductRepository;
import com.ttdat.productservice.infrastructure.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductQueryHandler {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @QueryHandler
    public ProductPageResult handle(GetProductPageQuery getProductPageQuery){
        Pageable pageable = PaginationUtils.getPageable(getProductPageQuery.getPaginationParams(), getProductPageQuery.getSortParams());
        Page<Product> productPage = productRepository.findAll(pageable);
        return ProductPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(productPage))
                .content(productMapper.toDTOList(productPage.getContent()))
                .build();
    }

    @QueryHandler
    public ProductDTO handle(GetProductByIdQuery getProductByIdQuery){
        Product product = productRepository.findById(getProductByIdQuery.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toDTO(product);
    }
}
