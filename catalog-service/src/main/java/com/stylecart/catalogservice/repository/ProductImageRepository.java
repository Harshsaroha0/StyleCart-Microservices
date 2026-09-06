package com.stylecart.catalogservice.repository;


import com.stylecart.catalogservice.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Long productId);

    Optional<ProductImage> findByProductIdAndPrimaryTrue(Long productId);

    boolean existsByProductIdAndDisplayOrder(Long productId, Integer displayOrder);
}
