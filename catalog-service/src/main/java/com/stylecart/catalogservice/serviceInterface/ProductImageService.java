package com.stylecart.catalogservice.serviceInterface;

import com.stylecart.catalogservice.dto.ProductImageRequest;
import com.stylecart.catalogservice.dto.ProductImageResponse;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse create(ProductImageRequest request);
    ProductImageResponse getById(long id);
    List<ProductImageResponse> getAll();
    ProductImageResponse update(Long id , ProductImageRequest request);
    void delete(Long id);
}
