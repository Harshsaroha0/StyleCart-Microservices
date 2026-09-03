package com.stylecart.catalogservice.controller;

import com.stylecart.catalogservice.dto.CategoryRequest;
import com.stylecart.catalogservice.dto.CategoryResponse;
import com.stylecart.catalogservice.serviceInterface.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {

        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponse> getById
            (@PathVariable long id) {

        return ResponseEntity.ok(categoryService.getById(id));

    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable long id , @RequestBody CategoryRequest request) {

        return ResponseEntity.ok(categoryService.update(id , request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
