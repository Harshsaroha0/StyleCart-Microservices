package com.stylecart.catalogservice.serviceImpl;


import com.stylecart.catalogservice.dto.ProductVariantRequest;
import com.stylecart.catalogservice.dto.ProductVariantResponse;
import com.stylecart.catalogservice.entity.Product;
import com.stylecart.catalogservice.entity.ProductVariant;
import com.stylecart.catalogservice.exception.NameAlreadyExistsException;
import com.stylecart.catalogservice.exception.ResourceNotFoundException;
import com.stylecart.catalogservice.repository.ProductRepository;
import com.stylecart.catalogservice.repository.ProductVariantRepository;
import com.stylecart.catalogservice.serviceInterface.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductVariantServiceImpl  implements ProductVariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;


    @Override
    public ProductVariantResponse create(ProductVariantRequest request) {

        //check if already present
        if (productVariantRepository
                .existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
                        request.getProductId(),
                        request.getSize(),
                        request.getColor())) {

            throw new NameAlreadyExistsException(
                    request.getSize() + " - " + request.getColor()
            );
        }

        //find product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product",
                                "id",
                                request.getProductId()
                        ));

        ProductVariant productVariant = new ProductVariant();

        productVariant.setProduct(product);
        productVariant.setColor(request.getColor());
        productVariant.setPrice(request.getPrice());
        productVariant.setSize(request.getSize());


        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);

        return ProductVariantResponse.builder()
                .id(savedProductVariant.getId())
                .productId(savedProductVariant.getProduct().getId())
                .productName(savedProductVariant.getProduct().getName())
                .size(savedProductVariant.getSize())
                .color(savedProductVariant.getColor())
                .price(savedProductVariant.getPrice())
                .build();
    }

    @Override
    public ProductVariantResponse getById(Long id) {

        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product variant not found "+ id));

        return ProductVariantResponse.builder()
                .id(productVariant.getId())
                .productId(productVariant.getProduct().getId())
                .productName(productVariant.getProduct().getName())
                .size(productVariant.getSize())
                .color(productVariant.getColor())
                .price(productVariant.getPrice())
                .build();
    }

    @Override
    public ProductVariantResponse update(Long id, ProductVariantRequest request) {

        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ProductVariant",
                                "id",
                                id
                        ));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product",
                                "id",
                                request.getProductId()
                        ));

        if (productVariantRepository
                .existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCaseAndIdNot(
                        request.getProductId(),
                        request.getSize(),
                        request.getColor(),
                        id)) {
            throw new NameAlreadyExistsException(
                    request.getSize() + " - " + request.getColor()
            );
        }

        productVariant.setProduct(product);
        productVariant.setSize(request.getSize());
        productVariant.setColor(request.getColor());
        productVariant.setPrice(request.getPrice());

        ProductVariant updatedProductVariant =
                productVariantRepository.save(productVariant);

        return new ProductVariantResponse(
                updatedProductVariant.getId(),
                updatedProductVariant.getProduct().getName(),
                updatedProductVariant.getProduct().getId(),
                updatedProductVariant.getSize(),
                updatedProductVariant.getColor(),
                updatedProductVariant.getPrice()
        );
    }


    @Override
    public List<ProductVariantResponse> getAll() {

        List<ProductVariant> productVariantList = productVariantRepository.findAll();

        return productVariantList.stream()
                .map(productVariant -> new ProductVariantResponse(
                        productVariant.getId(),
                        productVariant.getProduct().getName(),
                        productVariant.getProduct().getId(),
                        productVariant.getSize(),
                        productVariant.getColor(),
                        productVariant.getPrice()
                ))
                .toList();
    }

    @Override
    public void delete(Long id) {

        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product variant not found" + id));

        productVariantRepository.delete(productVariant);

    }
}
