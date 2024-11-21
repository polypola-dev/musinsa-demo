package com.musinsa.demo.controller;

import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.repository.BrandRepository;
import com.musinsa.demo.repository.CategoryRepository;
import com.musinsa.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CoordinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Brand brand1;
    private Brand brand2;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        // 브랜드 생성
        brand1 = brandRepository.save(Brand.builder()
                .name("테스트 브랜드1")
                .build());
        brand2 = brandRepository.save(Brand.builder()
                .name("테스트 브랜드2")
                .build());

        // 카테고리 생성
        category1 = categoryRepository.save(Category.builder()
                .name("테스트 카테고리1")
                .build());
        category2 = categoryRepository.save(Category.builder()
                .name("테스트 카테고리2")
                .build());

        // 상품 생성
        List<Product> products = Arrays.asList(
                createProduct(brand1, category1, 1000),
                createProduct(brand1, category2, 2000),
                createProduct(brand2, category1, 1500),
                createProduct(brand2, category2, 2500)
        );
        productRepository.saveAll(products);
    }

    @Test
    @DisplayName("카테고리별 최저가 상품 조회 API 테스트")
    void getCategoriesLowestPriceProductsTest() throws Exception {
        mockMvc.perform(get("/api/coordination/categories/lowest-prices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.totalPrice").exists());
    }

    @Test
    @DisplayName("브랜드별 최저가 상품 조회 API 테스트")
    void getBrandsLowestPriceProductsTest() throws Exception {
        mockMvc.perform(get("/api/coordination/brands/lowest-prices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lowestPrice.brandName").exists())
                .andExpect(jsonPath("$.lowestPrice.categories").isArray())
                .andExpect(jsonPath("$.lowestPrice.totalPrice").exists());
    }

    @Test
    @DisplayName("카테고리별 최저가/최고가 조회 API 테스트")
    void getLowestAndHighestPriceTest() throws Exception {
        String categoryName = "테스트 카테고리1";

        mockMvc.perform(get("/api/coordination/categories/{categoryName}/price-info", categoryName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value(categoryName))
                .andExpect(jsonPath("$.lowestPrice[0].brandName").exists())
                .andExpect(jsonPath("$.lowestPrice[0].price").exists())
                .andExpect(jsonPath("$.highestPrice[0].brandName").exists())
                .andExpect(jsonPath("$.highestPrice[0].price").exists());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 최저가/최고가 조회시 404 응답")
    void getLowestAndHighestPriceNotFoundTest() throws Exception {
        String nonExistentCategory = "존재하지 않는 카테고리";

        mockMvc.perform(get("/api/coordination/categories/{categoryName}/price-info", nonExistentCategory))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Product createProduct(Brand brand, Category category, int price) {
        return Product.builder()
                .name(brand.getName() + "_" + category.getName())
                .price(price)
                .brand(brand)
                .category(category)
                .build();
    }
} 