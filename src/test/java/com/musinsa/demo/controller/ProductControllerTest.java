package com.musinsa.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.demo.config.error.ErrorCode;
import com.musinsa.demo.dto.ProductDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.repository.BrandRepository;
import com.musinsa.demo.repository.CategoryRepository;
import com.musinsa.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.musinsa.demo.TestFixture.createProduct;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Brand brand;
    private Category category;

    @BeforeEach
    void setUp() {
        brand = brandRepository.save(Brand.builder()
                .name("테스트 브랜드")
                .build());

        category = categoryRepository.save(Category.builder()
                .name("테스트 카테고리")
                .build());
    }

    @Nested
    @DisplayName("상품 생성 테스트")
    class CreateProduct {
        @Test
        @DisplayName("성공")
        void success() throws Exception {
            ProductDto.Request request = ProductDto.Request.builder()
                    .name("테스트 상품")
                    .price(10000)
                    .brandId(brand.getId())    // TestFixture가 아닌 실제 저장된 brand ID 사용
                    .categoryId(category.getId())  // TestFixture가 아닌 실제 저장된 category ID 사용
                    .build();
            String content = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(request.getName()))
                    .andExpect(jsonPath("$.price").value(request.getPrice()))
                    .andExpect(jsonPath("$.brandId").value(brand.getId()))
                    .andExpect(jsonPath("$.categoryId").value(category.getId()));
        }

        @Test
        @DisplayName("실패 - 필수 파라미터 검증")
        void validationFail() throws Exception {
            ProductDto.Request request = new ProductDto.Request();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("실패 - 비활성화된 브랜드로 상품 생성")
        void inactiveBrandFail() throws Exception {
            // given
            brand.delete();  // 브랜드 비활성화
            ProductDto.Request request = ProductDto.Request.builder()
                    .name("테스트 상품")
                    .price(10000)
                    .brandId(brand.getId())
                    .categoryId(category.getId())
                    .build();
            String content = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ErrorCode.INACTIVE_BRAND.getMessage()));
        }

        @Test
        @DisplayName("실패 - 비활성화된 카테고리로 상품 생성")
        void inactiveCategoryFail() throws Exception {
            // given
            category.delete();  // 카테고리 비활성화
            ProductDto.Request request = ProductDto.Request.builder()
                    .name("테스트 상품")
                    .price(10000)
                    .brandId(brand.getId())
                    .categoryId(category.getId())
                    .build();
            String content = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ErrorCode.INACTIVE_CATEGORY.getMessage()));
        }
    }

    @Nested
    @DisplayName("상품 조회 테스트")
    class GetProduct {
        @Test
        @DisplayName("단건 조회 성공")
        void findByIdSuccess() throws Exception {
            Product product = productRepository.save(createProduct(brand, category));

            mockMvc.perform(get("/api/products/{id}", product.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(product.getId()))
                    .andExpect(jsonPath("$.name").value(product.getName()))
                    .andExpect(jsonPath("$.price").value(product.getPrice()))
                    .andExpect(jsonPath("$.brandId").value(brand.getId()))
                    .andExpect(jsonPath("$.categoryId").value(category.getId()));
        }

        @Test
        @DisplayName("비활성화 된 브랜드 상품은 브랜드 정보가 없다")
        void findWithDeletedBrand() throws Exception {
            Product product = productRepository.save(createProduct(brand, category));
            product.onBrandDeleted();

            mockMvc.perform(get("/api/products/{id}", product.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.brandId").isEmpty())
                    .andExpect(jsonPath("$.brandName").isEmpty());
        }

        @Test
        @DisplayName("비활성화 된 카테고리 상품은 카테고리 정보가 없다")
        void findWithDeletedCategory() throws Exception {
            Product product = productRepository.save(createProduct(brand, category));
            product.onCategoryDeleted();

            mockMvc.perform(get("/api/products/{id}", product.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.categoryId").isEmpty())
                    .andExpect(jsonPath("$.categoryName").isEmpty());
        }
    }

    @Nested
    @DisplayName("상품 수정 테스트")
    class UpdateProduct {
        @Test
        @DisplayName("성공")
        void success() throws Exception {
            Product product = productRepository.save(createProduct(brand, category));
            ProductDto.Request request = ProductDto.Request.builder()
                    .name("수정된 상품명")
                    .price(10000)
                    .brandId(brand.getId())
                    .categoryId(category.getId())
                    .build();

            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/api/products/{id}", product.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(request.getName()));
        }
    }

    @Nested
    @DisplayName("상품 삭제 테스트")
    class DeleteProduct {
        @Test
        @DisplayName("성공")
        void success() throws Exception {
            Product product = productRepository.save(createProduct(brand, category));

            mockMvc.perform(delete("/api/products/{id}", product.getId()))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

}