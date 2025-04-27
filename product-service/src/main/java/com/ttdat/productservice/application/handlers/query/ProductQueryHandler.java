package com.ttdat.productservice.application.handlers.query;

import com.ttdat.core.api.dto.response.ProductInfo;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.core.application.queries.inventory.GetProductCurrentStockQuery;
import com.ttdat.core.application.queries.product.GetProductInfoByIdQuery;
import com.ttdat.core.application.queries.product.GetProductStockQuantityQuery;
import com.ttdat.core.application.queries.product.SearchProductIdListQuery;
import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.api.dto.response.ProductPageResult;
import com.ttdat.productservice.application.mappers.ProductMapper;
import com.ttdat.productservice.application.mappers.UnitMapper;
import com.ttdat.productservice.application.queries.product.GetProductByIdQuery;
import com.ttdat.productservice.application.queries.product.GetProductPageQuery;
import com.ttdat.productservice.application.queries.product.SearchProductQuery;
import com.ttdat.productservice.domain.entities.Product;
import com.ttdat.productservice.domain.entities.ProductUnit;
import com.ttdat.productservice.domain.repositories.ProductRepository;
import com.ttdat.productservice.infrastructure.utils.PaginationUtils;
import com.ttdat.productservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.awt.geom.Arc2D;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductQueryHandler {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final QueryGateway queryGateway;

    @QueryHandler
    public ProductPageResult handle(GetProductPageQuery getProductPageQuery) {
        Pageable pageable = PaginationUtils.getPageable(getProductPageQuery.getPaginationParams(), getProductPageQuery.getSortParams());
        Specification<Product> productPageSpec = getProductSpec(getProductPageQuery.getFilterParams());
        Page<Product> productPage = productRepository.findAll(productPageSpec, pageable);
        return ProductPageResult.builder()
                .meta(PaginationUtils.getPaginationMeta(productPage))
                .content(productMapper.toDTOList(productPage.getContent()))
                .build();
    }

    private Specification<Product> getProductSpec(Map<String, String> filterParams) {
        Specification<Product> productPageSpec = Specification.where(null);
        productPageSpec = productPageSpec.and(SpecificationUtils.buildJoinSpecification(filterParams, "category", "categoryId", Long.class));
        if (filterParams.containsKey("query")) {
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

    private Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @QueryHandler
    public ProductDTO handle(GetProductByIdQuery getProductByIdQuery) {
        Product product = getProductById(getProductByIdQuery.getProductId());
        return productMapper.toDTO(product);
    }

    @QueryHandler
    public List<ProductDTO> handle(SearchProductQuery searchProductQuery, QueryMessage<?, ?> queryMessage) {
        Long warehouseId = (Long) queryMessage.getMetaData().get("warehouseId");
        Specification<Product> productSpec = getProductSpec(Map.of("query", searchProductQuery.getQuery()));
        List<Product> products = productRepository.findAll(productSpec);
        return products.stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDTO(product);
                    GetProductCurrentStockQuery getProductCurrentStockQuery = GetProductCurrentStockQuery.builder()
                            .productId(product.getProductId())
                            .warehouseId(warehouseId)
                            .build();
                    Double currentStock = queryGateway.query(getProductCurrentStockQuery, ResponseTypes.instanceOf(Double.class)).join();
                    productDTO.setCurrentStock(currentStock);
                    return productDTO;
                })
                .toList();
    }

    @QueryHandler
    public List<String> handle(SearchProductIdListQuery searchProductIdListQuery) {
        Specification<Product> productSpec = getProductSpec(searchProductIdListQuery.getFilterParams());
        List<Product> products = productRepository.findAll(productSpec);
        return products.stream().map(Product::getProductId).toList();
    }

    @QueryHandler
    public ProductInfo handle(GetProductInfoByIdQuery getProductInfoByIdQuery) {
        Product product = getProductById(getProductInfoByIdQuery.getProductId());
        return productMapper.toProductInfo(product);
    }

    @QueryHandler
    public Double handle(GetProductStockQuantityQuery getProductStockQuantityQuery) {
        Product product = getProductById(getProductStockQuantityQuery.getProductId());
        Double currentConversionFactor = product.getProductUnits().stream()
                .filter(productUnit -> productUnit.getProductUnitId().equals(getProductStockQuantityQuery.getProductUnitId()))
                .findFirst()
                .map(ProductUnit::getConversionFactor)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.UNIT_NOT_FOUND));
        Double defaultConversionFactor = product.getProductUnits().stream()
                .filter(ProductUnit::isDefault)
                .findFirst()
                .map(ProductUnit::getConversionFactor)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.UNIT_NOT_FOUND));
        return getProductStockQuantityQuery.getQuantity() * currentConversionFactor / defaultConversionFactor;
    }
}
