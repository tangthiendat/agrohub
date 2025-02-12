package com.ttdat.productservice.application.services;

import com.ttdat.productservice.api.dto.common.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    void createProduct(ProductDTO productDTO, MultipartFile productImg) throws IOException;
}
