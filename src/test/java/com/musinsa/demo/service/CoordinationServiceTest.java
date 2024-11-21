package com.musinsa.demo.service;

import com.musinsa.demo.config.error.exception.NotFoundException;
import com.musinsa.demo.dto.coordination.BrandsLowestPriceDto;
import com.musinsa.demo.dto.coordination.CategoriesLowestPriceDto;
import com.musinsa.demo.dto.coordination.CategoryPriceInfoDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.repository.BrandRepository;
import com.musinsa.demo.repository.CategoryRepository;
import com.musinsa.demo.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.musinsa.demo.TestFixture.createBrand;
import static com.musinsa.demo.TestFixture.createCategory;
import static com.musinsa.demo.TestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CoordinationServiceTest {

    @InjectMocks
    private CoordinationService coordinationService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("카테고리별 최저가 상품 조회 성공")
    void findCategoriesLowestPriceProductsSuccess() {
        // given
        Category category1 = createCategory();
        Category category2 = createCategory();
        List<Category> categories = Arrays.asList(category1, category2);

        Product product1 = createProduct(createBrand(), category1);
        Product product2 = createProduct(createBrand(), category2);

        given(categoryRepository.findAll()).willReturn(categories);
        given(productRepository.findByCategoryLowestPrice(category1.getId())).willReturn(Optional.of(product1));
        given(productRepository.findByCategoryLowestPrice(category2.getId())).willReturn(Optional.of(product2));

        // when
        CategoriesLowestPriceDto.Response response = coordinationService.findCategoriesLowestPriceProducts();

        // then
        assertThat(response.getItems()).hasSize(2);
        assertThat(response.getTotalPrice()).isNotNull();
    }

    @Test
    @DisplayName("브랜드별 최저가 상품 조회 성공")
    void findBrandsLowestPriceProductsSuccess() {
        // given
        Brand brand1 = createBrand();
        Brand brand2 = createBrand();
        List<Brand> brands = Arrays.asList(brand1, brand2);

        Category category1 = createCategory();
        Category category2 = createCategory();
        List<Category> categories = Arrays.asList(category1, category2);

        Product product1 = createProduct(brand1, category1);
        Product product2 = createProduct(brand1, category2);

        given(brandRepository.findAll()).willReturn(brands);
        given(categoryRepository.findAll()).willReturn(categories);
        given(productRepository.findFirstByBrand_IdAndCategory_IdOrderByPriceAscIdDesc(brand1.getId(), category1.getId()))
                .willReturn(Optional.of(product1));
        given(productRepository.findFirstByBrand_IdAndCategory_IdOrderByPriceAscIdDesc(brand1.getId(), category2.getId()))
                .willReturn(Optional.of(product2));
        given(productRepository.findFirstByBrand_IdAndCategory_IdOrderByPriceAscIdDesc(brand2.getId(), category1.getId()))
                .willReturn(Optional.of(createProduct(brand2, category1)));
        given(productRepository.findFirstByBrand_IdAndCategory_IdOrderByPriceAscIdDesc(brand2.getId(), category2.getId()))
                .willReturn(Optional.of(createProduct(brand2, category2)));

        // when
        BrandsLowestPriceDto.Response response = coordinationService.findBrandsLowestPriceProducts();

        // then
        assertThat(response.getLowestPrice()).isNotNull();
        assertThat(response.getLowestPrice().getCategories()).isNotEmpty();
    }

    @Test
    @DisplayName("카테고리별 최저가/최고가 조회 성공")
    void findLowestAndHighestPriceSuccess() {
        // given
        String categoryName = "테스트 카테고리";
        Category category = createCategory();
        Product lowestProduct = createProduct(createBrand(), category);
        Product highestProduct = createProduct(createBrand(), category);

        given(categoryRepository.findByName(categoryName)).willReturn(Optional.of(category));
        given(productRepository.findByCategoryLowestPrice(category.getId())).willReturn(Optional.of(lowestProduct));
        given(productRepository.findByCategoryHighestPrice(category.getId())).willReturn(Optional.of(highestProduct));

        // when
        CategoryPriceInfoDto.Response response = coordinationService.findLowestAndHighestPrice(categoryName);

        // then
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        assertThat(response.getLowestPrice()).isNotEmpty();
        assertThat(response.getHighestPrice()).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 최저가/최고가 조회 시 예외 발생")
    void findLowestAndHighestPriceWithNonExistentCategoryFail() {
        // given
        String categoryName = "존재하지 않는 카테고리";
        given(categoryRepository.findByName(categoryName)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> coordinationService.findLowestAndHighestPrice(categoryName))
                .isInstanceOf(NotFoundException.class);
    }
} 