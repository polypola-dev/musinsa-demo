package com.musinsa.demo;

import com.musinsa.demo.dto.ProductDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.entity.status.ProductStatus;
import com.musinsa.demo.entity.status.Status;
import org.springframework.test.util.ReflectionTestUtils;

public class TestFixture {
    
    public static ProductDto.Request createProductRequest() {
        return ProductDto.Request.builder()
            .name("테스트 상품")
            .price(10000)
            .brandId(1L)
            .categoryId(1L)
            .build();
    }
    
    public static Brand createBrand() {
        Brand brand = Brand.builder()
            .name("테스트 브랜드")
            .status(Status.ACTIVE)
            .build();
        ReflectionTestUtils.setField(brand, "id", 1L);
        return brand;
    }
    
    public static Brand createBrand(String name) {
        Brand brand = Brand.builder()
            .name(name)
            .status(Status.ACTIVE)
            .build();
        ReflectionTestUtils.setField(brand, "id", 1L);
        return brand;
    }
    
    public static Brand createBrand(Long id) {
        Brand brand = Brand.builder()
            .name("테스트 브랜드")
            .status(Status.ACTIVE)
            .build();
        ReflectionTestUtils.setField(brand, "id", id);
        return brand;
    }
    
    public static Category createCategory() {
        Category category = Category.builder()
            .name("테스트 카테고리")
            .status(Status.ACTIVE)
            .build();
        ReflectionTestUtils.setField(category, "id", 1L);
        return category;
    }
    
    public static Category createCategory(String name) {
        Category category = Category.builder()
            .name(name)
            .status(Status.ACTIVE)
            .build();
        ReflectionTestUtils.setField(category, "id", 1L);
        return category;
    }
    
    public static Category createCategory(Long id) {
        Category category = Category.builder()
            .name("테스트 카테고리")
            .status(Status.ACTIVE)
            .build();
        ReflectionTestUtils.setField(category, "id", id);
        return category;
    }
    
    public static Product createProduct(Brand brand, Category category) {
        Product product = Product.builder()
            .name("테스트 상품")
            .price(10000)
            .status(ProductStatus.ACTIVE)
            .brand(brand)
            .category(category)
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);
        return product;
    }
    
    public static Product createProduct(Brand brand, Category category, int price) {
        Product product = Product.builder()
            .name("테스트 상품")
            .price(price)
            .status(ProductStatus.ACTIVE)
            .brand(brand)
            .category(category)
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);
        return product;
    }
    
    public static Product createProduct(String name, int price, Brand brand, Category category) {
        Product product = Product.builder()
            .name(name)
            .price(price)
            .status(ProductStatus.ACTIVE)
            .brand(brand)
            .category(category)
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);
        return product;
    }
    
    public static Product createProductWithStatus(Brand brand, Category category, ProductStatus status) {
        Product product = Product.builder()
            .name("테스트 상품")
            .price(10000)
            .status(status)
            .brand(brand)
            .category(category)
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);
        return product;
    }
}