package com.stylecart.catalogservice.serviceImpl;

import com.stylecart.catalogservice.dto.ProductRequest;
import com.stylecart.catalogservice.dto.ProductResponse;
import com.stylecart.catalogservice.entity.Category;
import com.stylecart.catalogservice.entity.Product;
import com.stylecart.catalogservice.exception.NameAlreadyExistsException;
import com.stylecart.catalogservice.exception.ResourceNotFoundException;
import com.stylecart.catalogservice.repository.CategoryRepository;
import com.stylecart.catalogservice.repository.ProductRepository;
import com.stylecart.catalogservice.serviceInterface.ProductService;
import com.stylecart.catalogservice.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public ProductResponse create(ProductRequest request) {

        //check if already present
        if(productRepository.existsByName(request.getName())) {
            throw new NameAlreadyExistsException(request.getName());
        }

        //find category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getStockQuantity(),
                savedProduct.getCategory().getId(),
                savedProduct.getCategory().getName()
        );
    }

    @Override
    public ProductResponse getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("product not found" + id));

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

    @Override
    public List<ProductResponse> getAll() {

        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getCategory().getId(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("product", "id", id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "category",
                                "id",
                                request.getCategoryId()
                        ));

        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getStockQuantity(),
                updatedProduct.getCategory().getId(),
                updatedProduct.getCategory().getName()
        );
    }

    @Override
    public void delete(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product" , "id" , id));

        productRepository.delete(product);

    }

    @Override
    public Page<ProductResponse> filterProducts(String search, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice , Pageable pageable) {


        Specification<Product> specification = Specification.allOf();

        if (search != null && !search.isBlank()) {
            specification = specification.and(
                    ProductSpecification.hasName(search)
            );
        }

        if (categoryId != null) {
            specification = specification.and(
                    ProductSpecification.hasCategory(categoryId)
            );
        }

        if (minPrice != null) {
            specification = specification.and(
                    ProductSpecification.hasMinPrice(minPrice)
            );
        }

        if (maxPrice != null) {
            specification = specification.and(
                    ProductSpecification.hasMaxPrice(maxPrice)
            );
        }

        return productRepository
                .findAll(specification, pageable)
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getCategory().getId(),
                        product.getCategory().getName()
                ));
    }
}
