package com.stylecart.catalogservice.repository;


import com.stylecart.catalogservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    boolean existsByName(String name);

}