package com.stylecart.catalogservice.serviceImpl;

import com.stylecart.catalogservice.dto.CategoryRequest;
import com.stylecart.catalogservice.dto.CategoryResponse;
import com.stylecart.catalogservice.entity.Category;
import com.stylecart.catalogservice.exception.NameAlreadyExistsException;
import com.stylecart.catalogservice.exception.ResourceNotFoundException;
import com.stylecart.catalogservice.repository.CategoryRepository;
import com.stylecart.catalogservice.serviceInterface.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        //check if name already exists
        if(categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new NameAlreadyExistsException(request.getName());
        }

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription()
        );
    }

    @Override
    public CategoryResponse getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("category not found " + id));

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    @Override
    public List<CategoryResponse> getAll(){

        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                ))
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Category", "id", id));

        if (categoryRepository.existsByNameIgnoreCase(request.getName())
                && !category.getName().equalsIgnoreCase(request.getName())) {

            throw new NameAlreadyExistsException(request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                updatedCategory.getId(),
                updatedCategory.getName(),
                updatedCategory.getDescription()
        );
    }

    @Override
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("category" , "id" , id));

        categoryRepository.delete(category);
    }
}
