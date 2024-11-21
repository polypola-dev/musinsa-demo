package com.musinsa.demo.service;

import com.musinsa.demo.config.error.ErrorCode;
import com.musinsa.demo.config.error.exception.DuplicateException;
import com.musinsa.demo.config.error.exception.NotFoundException;
import com.musinsa.demo.dto.CategoryDto;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto.Response create(CategoryDto.Request request) {
        validateDuplicateCategoryName(request.getName());

        Category category = Category.builder()
                .name(request.getName())
                .build();
        return CategoryDto.Response.from(categoryRepository.save(category));
    }

    public CategoryDto.Response findById(Long id) {
        return CategoryDto.Response.from(findCategoryById(id));
    }

    public List<CategoryDto.Response> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto.Response update(Long id, CategoryDto.Request request) {
        validateDuplicateCategoryNameExceptSelf(request.getName(), id);

        Category category = findCategoryById(id);
        category.updateName(request.getName());
        return CategoryDto.Response.from(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findCategoryById(id);
        category.delete();
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private void validateDuplicateCategoryName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
    }

    private void validateDuplicateCategoryNameExceptSelf(String name, Long id) {
        if (categoryRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
    }
} 