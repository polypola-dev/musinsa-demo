package com.musinsa.demo.entity;

import com.musinsa.demo.entity.status.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.musinsa.demo.TestFixture.createBrand;
import static com.musinsa.demo.TestFixture.createCategory;
import static com.musinsa.demo.TestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("상품 생성 시 기본 상태는 ACTIVE")
    void createProductWithDefaultStatus() {
        // when
        Product product = Product.builder()
                .name("테스트 상품")
                .price(10000)
                .build();

        // then
        assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @DisplayName("상품 전체 정보 업데이트 성공")
    void updateProduct() {
        // given
        Product product = createProduct(createBrand(), createCategory());
        String newName = "새 상품명";
        int newPrice = 20000;
        Brand newBrand = createBrand("새 브랜드");
        Category newCategory = createCategory("새 카테고리");

        // when
        product.update(newName, newPrice, newBrand, newCategory);

        // then
        assertThat(product.getName()).isEqualTo(newName);
        assertThat(product.getPrice()).isEqualTo(newPrice);
        assertThat(product.getBrand()).isEqualTo(newBrand);
        assertThat(product.getCategory()).isEqualTo(newCategory);
    }

    @Test
    @DisplayName("상품 삭제 시 상태가 INACTIVE로 변경")
    void deleteProduct() {
        // given
        Product product = createProduct(createBrand(), createCategory());

        // when
        product.delete();

        // then
        assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
    }

    @Test
    @DisplayName("브랜드 삭제 시, 상품의 브랜드 참조가 null로 변경")
    void onBrandDeleted() {
        // given
        Product product = createProduct(createBrand(), createCategory());

        // when
        product.onBrandDeleted();

        // then
        assertThat(product.getBrand()).isNull();
    }

    @Test
    @DisplayName("카테고리 삭제 시, 상품의 카테고리 참조가 null로 변경")
    void onCategoryDeleted() {
        // given
        Product product = createProduct(createBrand(), createCategory());

        // when
        product.onCategoryDeleted();

        // then
        assertThat(product.getCategory()).isNull();
    }
} 