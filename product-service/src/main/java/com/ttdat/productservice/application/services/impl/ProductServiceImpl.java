package com.ttdat.productservice.application.services.impl;

import com.ttdat.productservice.api.dto.common.ProductDTO;
import com.ttdat.productservice.application.commands.product.CmdProductUnit;
import com.ttdat.productservice.application.commands.product.CmdProductUnitPrice;
import com.ttdat.productservice.application.commands.product.CreateProductCommand;
import com.ttdat.productservice.application.services.ProductService;
import com.ttdat.productservice.infrastructure.services.CloudinaryService;
import com.ttdat.productservice.infrastructure.utils.BarcodeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CommandGateway commandGateway;
    private final CloudinaryService cloudinaryService;

    @Override
    public void createProduct(ProductDTO productDTO, MultipartFile productImg) throws IOException {
        String productId = BarcodeUtils.generateEAN13Barcode();
        String imageUrl = cloudinaryService.upload(productImg);
        List<CmdProductUnit> cmdProductUnits = productDTO.getProductUnits() != null ?
                productDTO.getProductUnits().stream()
                        .map(productUnit -> {
                            List<CmdProductUnitPrice> cmdProductUnitPrices = productUnit.getProductUnitPrices() != null ?
                                    productUnit.getProductUnitPrices().stream()
                                            .map(productUnitPrice -> CmdProductUnitPrice.builder()
                                                    .productUnitPriceId(RandomStringUtils.secure().nextAlphanumeric(12))
                                                    .price(productUnitPrice.getPrice())
                                                    .validFrom(productUnitPrice.getValidFrom())
                                                    .validTo(productUnitPrice.getValidTo())
                                                    .build())
                                            .toList()
                                    : List.of();
                            return CmdProductUnit.builder()
                                    .productUnitId(RandomStringUtils.secure().nextAlphanumeric(12))
                                    .unitId(productUnit.getUnit().getUnitId())
                                    .conversionFactor(productUnit.getConversionFactor())
                                    .isDefault(productUnit.isDefault())
                                    .productUnitPrices(cmdProductUnitPrices)
                                    .build();
                        })
                        .toList()
                : List.of();
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(productId)
                .productName(productDTO.getProductName())
                .description(productDTO.getDescription())
                .imageUrl(imageUrl)
                .categoryId(productDTO.getCategory().getCategoryId())
                .defaultExpDays(productDTO.getDefaultExpDays())
                .storageInstructions(productDTO.getStorageInstructions())
                .productUnits(cmdProductUnits)
                .physicalState(productDTO.getPhysicalState())
                .packaging(productDTO.getPackaging())
                .safetyInstructions(productDTO.getSafetyInstructions())
                .hazardClassification(productDTO.getHazardClassification())
                .ppeRequired(productDTO.getPpeRequired())
                .build();
        commandGateway.sendAndWait(createProductCommand);
    }

}
