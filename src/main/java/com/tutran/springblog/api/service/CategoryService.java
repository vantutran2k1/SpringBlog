package com.tutran.springblog.api.service;

import com.tutran.springblog.api.entity.Category;
import com.tutran.springblog.api.payload.category.CategoryRequestDto;
import com.tutran.springblog.api.payload.category.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto getCategoryById(long categoryId);

    Category getCategoryByIdOrThrowException(long categoryId);
}
