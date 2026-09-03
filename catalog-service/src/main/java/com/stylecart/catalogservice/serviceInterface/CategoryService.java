package com.stylecart.catalogservice.serviceInterface;


import com.stylecart.catalogservice.dto.CategoryRequest;
import com.stylecart.catalogservice.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    CategoryResponse update ( Long id ,CategoryRequest request);

    void delete(Long id);
}
