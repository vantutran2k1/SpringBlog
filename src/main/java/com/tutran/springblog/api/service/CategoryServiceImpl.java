package com.tutran.springblog.api.service;

import com.tutran.springblog.api.mapper.CategoryMapper;
import com.tutran.springblog.api.payload.category.CategoryRequestDto;
import com.tutran.springblog.api.payload.category.CategoryResponseDto;
import com.tutran.springblog.api.repository.CategoryRepository;
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
}
