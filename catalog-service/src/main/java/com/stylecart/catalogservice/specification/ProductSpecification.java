package com.stylecart.catalogservice.specification;


import com.stylecart.catalogservice.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {


    public static Specification<Product> hasName(String keyword) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategory(Long categoryId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("category").get("id"),
                        categoryId
                );
    }

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        minPrice
                );
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        maxPrice
                );
    }


}
