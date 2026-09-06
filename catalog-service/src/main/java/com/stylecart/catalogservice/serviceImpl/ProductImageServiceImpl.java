package com.stylecart.catalogservice.serviceImpl;


import com.stylecart.catalogservice.dto.ProductImageRequest;
import com.stylecart.catalogservice.dto.ProductImageResponse;
import com.stylecart.catalogservice.entity.Product;
import com.stylecart.catalogservice.entity.ProductImage;
import com.stylecart.catalogservice.exception.ResourceNotFoundException;
import com.stylecart.catalogservice.repository.ProductImageRepository;
import com.stylecart.catalogservice.repository.ProductRepository;
import com.stylecart.catalogservice.serviceInterface.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductImageResponse create(ProductImageRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + request.getProductId()
                        )
                );

        if(request.isPrimary()) {
            productImageRepository
                    .findByProductIdAndPrimaryTrue(product.getId())
                    .ifPresent(existingPrimary -> {
                        existingPrimary.setPrimary(false);
                    });
        }

        ProductImage productImage = new ProductImage();

        productImage.setProduct(product);
        productImage.setImageUrl(request.getImageUrl());
        productImage.setPrimary(request.isPrimary());
        productImage.setDisplayOrder(request.getDisplayOrder());

        ProductImage savedImage = productImageRepository.save(productImage);

        return mapToResponse(savedImage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponse getById(long id) {

        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product-image not found" + id));

        return mapToResponse(productImage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getAll() {

        return productImageRepository.findAll()
                .stream()
                .map(this :: mapToResponse)
                .toList();

    }

    @Override
    public ProductImageResponse update(Long id, ProductImageRequest request) {

        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(";product image not found" + id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("product not found" + id));

        if (request.isPrimary()) {

            productImageRepository
                    .findByProductIdAndPrimaryTrue(product.getId())
                    .ifPresent(existingPrimary -> {

                        if (!existingPrimary.getId()
                                .equals(productImage.getId())) {

                            existingPrimary.setPrimary(false);
                        }
                    });
        }

        productImage.setProduct(product);
        productImage.setImageUrl(request.getImageUrl());
        productImage.setPrimary(request.isPrimary());
        productImage.setDisplayOrder(request.getDisplayOrder());

        ProductImage updatedImage =
                productImageRepository.save(productImage);

        return mapToResponse(updatedImage);
    }

    @Override
    public void delete(Long id) {

        ProductImage productImage =
                productImageRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product image not found with id: " + id
                                )
                        );

        Product product = productImage.getProduct();

        boolean wasPrimary = productImage.isPrimary();

        productImageRepository.delete(productImage);

        if (wasPrimary) {

            List<ProductImage> remainingImages =
                    productImageRepository
                            .findByProductIdOrderByDisplayOrderAsc(
                                    product.getId()
                            );

            if (!remainingImages.isEmpty()) {

                ProductImage newPrimary =
                        remainingImages.get(0);

                newPrimary.setPrimary(true);
            }
        }

    }

    private ProductImageResponse mapToResponse(
            ProductImage productImage) {

        Product product = productImage.getProduct();

        ProductImageResponse response =
                new ProductImageResponse();

        response.setId(productImage.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setImageUrl(productImage.getImageUrl());
        response.setPrimary(productImage.isPrimary());
        response.setDisplayOrder(productImage.getDisplayOrder());

        return response;
    }
}
