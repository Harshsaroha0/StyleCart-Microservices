package com.stylecart.catalogservice.repository;

import com.stylecart.catalogservice.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    boolean existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
            Long productId,
            String size,
            String color
    );

    boolean existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCaseAndIdNot(
            Long productId,
            String size,
            String color,
            Long id
    );
}
