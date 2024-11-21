package com.musinsa.demo.entity;

import com.musinsa.demo.entity.status.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.musinsa.demo.TestFixture.createBrand;
import static com.musinsa.demo.TestFixture.createCategory;
import static com.musinsa.demo.TestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

class BrandTest {

    @Test
    @DisplayName("브랜드 생성 시 기본 상태는 ACTIVE")
    void createBrandWithDefaultStatus() {
        // when
        Brand brand = Brand.builder()
                .name("테스트 브랜드")
                .build();

        // then
        assertThat(brand.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("브랜드 삭제 시 상태가 INACTIVE로 변경되고 연관된 상품의 브랜드 참조가 제거됨")
    void deleteBrand() {
        // given
        Brand brand = createBrand();
        Product product = createProduct(brand, createCategory());
        brand.getProducts().add(product);

        // when
        brand.delete();

        // then
        assertThat(brand.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(product.getBrand()).isNull();
    }
}