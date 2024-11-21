package com.musinsa.demo.service;

import com.musinsa.demo.config.error.exception.BusinessException;
import com.musinsa.demo.dto.CategoryDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.entity.status.Status;
import com.musinsa.demo.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 생성 시 이름 중복 검증")
    void validateDuplicateCategoryNameOnCreate() {
        // given
        CategoryDto.Request request = CategoryDto.Request.builder()
                .name("테스트 카테고리")
                .build();

        given(categoryRepository.existsByName(request.getName())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 존재하는 카테고리명입니다");
    }

    private Category createCategory() {
        return Category.builder()
                .name("테스트 카테고리")
                .build();
    }

    private Product createProduct(Category category) {
        return Product.builder()
                .name("테스트 상품")
                .price(10000)
                .brand(createBrand())
                .category(category)
                .build();
    }

    private Brand createBrand() {
        return Brand.builder()
                .name("테스트 브랜드")
                .build();
    }

    @Test
    @DisplayName("카테고리 수정 시 이름 중복 검증")
    void validateDuplicateCategoryNameOnUpdateName() {
        // given
        Long categoryId = 1L;
        CategoryDto.Request request = CategoryDto.Request.builder()
                .name("테스트 카테고리")
                .build();

        given(categoryRepository.existsByNameAndIdNot(request.getName(), categoryId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.update(categoryId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 존재하는 카테고리명입니다");
    }

    @Test
    @DisplayName("카테고리 삭제시 연관된 상품들의 카테고리 참조가 제거됨")
    void deleteCategoryWithProducts() {
        // given
        Long categoryId = 1L;
        Category category = createCategory();
        Product product1 = createProduct(category);
        Product product2 = createProduct(category);
        category.getProducts().add(product1);
        category.getProducts().add(product2);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        // when
        categoryService.delete(categoryId);

        // then
        assertThat(product1.getCategory()).isNull();
        assertThat(product2.getCategory()).isNull();
        assertThat(category.getStatus()).isEqualTo(Status.INACTIVE);
    }
} 