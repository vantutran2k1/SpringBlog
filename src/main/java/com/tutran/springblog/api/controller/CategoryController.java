package com.tutran.springblog.api.controller;

import com.tutran.springblog.api.payload.ApiResponse;
import com.tutran.springblog.api.payload.category.CategoryRequestDto;
import com.tutran.springblog.api.payload.category.CategoryResponseDto;
import com.tutran.springblog.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> addCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        ApiResponse<CategoryResponseDto> apiResponse = new ApiResponse<>(categoryService.addCategory(categoryRequestDto));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable(name = "id") long id) {
        ApiResponse<CategoryResponseDto> apiResponse = new ApiResponse<>(categoryService.getCategoryById(id));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        ApiResponse<List<CategoryResponseDto>> apiResponse = new ApiResponse<>(categoryService.getAllCategories());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto,
            @PathVariable(name = "id") long id
    ) {
        ApiResponse<CategoryResponseDto> apiResponse = new ApiResponse<>(
                categoryService.updateCategory(categoryRequestDto, id)
        );
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCategoryById(@PathVariable(name = "id") long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>(categoryService.deleteCategory(id));
        return ResponseEntity.ok(apiResponse);
    }
}
