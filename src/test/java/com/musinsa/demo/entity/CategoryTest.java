package com.musinsa.demo.entity;

import com.musinsa.demo.entity.status.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.musinsa.demo.TestFixture.createBrand;
import static com.musinsa.demo.TestFixture.createCategory;
import static com.musinsa.demo.TestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    @DisplayName("카테고리 생성 시 기본 상태는 ACTIVE")
    void createCategoryWithDefaultStatus() {
        // when
        Category category = Category.builder()
                .name("테스트 카테고리")
                .build();

        // then
        assertThat(category.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("카테고리 삭제 시 상태가 INACTIVE로 변경되고 연관된 상품의 카테고리 참조가 제거됨")
    void deleteCategory() {
        // given
        Category category = createCategory();
        Product product = createProduct(createBrand(), category);
        category.getProducts().add(product);

        // when
        category.delete();

        // then
        assertThat(category.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(product.getCategory()).isNull();
    }
} 