package com.ttdat.productservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.api.dto.response.ProductPageResult;
import com.ttdat.productservice.application.mappers.ProductMapper;
import com.ttdat.productservice.application.queries.product.GetProductByIdQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.productservice.application.queries.product.GetProductPageQuery;
import com.ttdat.productservice.application.queries.product.SearchProductQuery;
import com.ttdat.productservice.domain.entities.Product;
import com.ttdat.productservice.domain.repositories.ProductRepository;
import com.ttdat.productservice.infrastructure.utils.PaginationUtils;
import com.ttdat.productservice.infrastructure.utils.SpecificationUtils;
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
public class ProductQueryHandler {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @QueryHandler
    public ProductPageResult handle(GetProductPageQuery getProductPageQuery){
        Pageable pageable = PaginationUtils.getPageable(getProductPageQuery.getPaginationParams(), getProductPageQuery.getSortParams());
        Specification<Product> productPageSpec = getProductSpec(getProductPageQuery.getFilterParams());
        Page<Product> productPage = productRepository.findAll(productPageSpec, pageable);
        return ProductPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(productPage))
                .content(productMapper.toDTOList(productPage.getContent()))
                .build();
    }

    private Specification<Product> getProductSpec(Map<String, String> filterParams){
        Specification<Product> productPageSpec = Specification.where(null);
        productPageSpec = productPageSpec.and(SpecificationUtils.buildJoinSpecification(filterParams, "category", "categoryId", Long.class));
        if (filterParams.containsKey("query")){
            String searchValue = filterParams.get("query").toLowerCase();
            Specification<Product> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("productId"), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("categoryName")), likePattern)
                );
            };
            productPageSpec = productPageSpec.and(querySpec);
        }
        return productPageSpec;
    }

    @QueryHandler
    public ProductDTO handle(GetProductByIdQuery getProductByIdQuery){
        Product product = productRepository.findById(getProductByIdQuery.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toDTO(product);
    }

    @QueryHandler
    public List<ProductDTO> handle(SearchProductQuery searchProductQuery){
        Specification<Product> productSpec = getProductSpec(Map.of("query", searchProductQuery.getQuery()));
        List<Product> products = productRepository.findAll(productSpec);
        return productMapper.toDTOList(products);
    }

    @QueryHandler
    public ProductInfo handle(GetProductInfoByIdQuery getProductInfoByIdQuery){
        Product product = productRepository.findById(getProductInfoByIdQuery.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductInfo(product);
    }
}
