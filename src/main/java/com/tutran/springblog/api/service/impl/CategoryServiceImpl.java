package com.tutran.springblog.api.service.impl;

import com.tutran.springblog.api.entity.Category;
import com.tutran.springblog.api.mapper.CategoryMapper;
import com.tutran.springblog.api.payload.category.CategoryRequestDto;
import com.tutran.springblog.api.payload.category.CategoryResponseDto;
import com.tutran.springblog.api.repository.CategoryRepository;
import com.tutran.springblog.api.service.CategoryService;
import com.tutran.springblog.api.utils.ErrorMessageBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        var category = categoryMapper.categoryRequestDtoToCategory(categoryRequestDto);
        var newCategory = categoryRepository.save(category);

        return categoryMapper.categoryToCategoryResponseDto(newCategory);
    }

    @Override
    public CategoryResponseDto getCategoryById(long categoryId) {
        return categoryMapper.categoryToCategoryResponseDto(getCategoryByIdOrThrowException(categoryId));
    }

    @Override
    public Category getCategoryByIdOrThrowException(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessageBuilder.getCategoryNotFoundErrorMessage(categoryId))
        );
    }
}
