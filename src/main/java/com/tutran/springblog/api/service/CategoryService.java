package com.tutran.springblog.api.service;

import com.tutran.springblog.api.entity.Category;
import com.tutran.springblog.api.payload.category.CategoryRequestDto;
import com.tutran.springblog.api.payload.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto getCategoryById(long categoryId);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, long categoryId);

    String deleteCategory(long categoryId);

    Category getCategoryByIdOrThrowException(long categoryId);
}
