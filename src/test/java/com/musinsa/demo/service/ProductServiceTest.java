package com.musinsa.demo.service;

import com.musinsa.demo.config.error.ErrorCode;
import com.musinsa.demo.config.error.exception.BusinessException;
import com.musinsa.demo.config.error.exception.NotFoundException;
import com.musinsa.demo.dto.ProductDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.entity.status.Status;
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
import static com.musinsa.demo.TestFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품 생성 성공")
    void createProductSuccess() {
        // given
        ProductDto.Request request = createProductRequest();
        Brand brand = createBrand();
        Category category = createCategory();
        Product product = createProduct(brand, category);

        given(brandRepository.findById(request.getBrandId())).willReturn(Optional.of(brand));
        given(categoryRepository.findById(request.getCategoryId())).willReturn(Optional.of(category));
        given(productRepository.save(any(Product.class))).willReturn(product);

        // when
        ProductDto.Response response = productService.create(request);

        // then
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getPrice()).isEqualTo(request.getPrice());
        assertThat(response.getBrandId()).isEqualTo(request.getBrandId());
        assertThat(response.getCategoryId()).isEqualTo(request.getCategoryId());
        
        verify(brandRepository).findById(request.getBrandId());
        verify(categoryRepository).findById(request.getCategoryId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 단일 조회 성공")
    void findByIdSuccess() {
        // given
        Long productId = 1L;
        Product product = createProduct(createBrand(), createCategory());

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        ProductDto.Response response = productService.findById(productId);

        // then
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getPrice()).isEqualTo(product.getPrice());
        assertThat(response.getBrandId()).isEqualTo(product.getBrandId());
        assertThat(response.getCategoryId()).isEqualTo(product.getCategoryId());

        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("상품 전체 조회 성공")
    void findAllSuccess() {
        // given
        List<Product> products = Arrays.asList(
            createProduct(createBrand(), createCategory()),
            createProduct(createBrand(), createCategory())
        );
        
        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductDto.Response> responses = productService.findAll();

        // then
        assertThat(responses).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("비활성화된 브랜드로 업데이트 시 실패")
    void updateBrandWithInactiveBrandFail() {
        // given
        ProductDto.Request request = createProductRequest();

        Brand activeBrand = createBrand();
        Category activeCategory = createCategory();

        Brand inactiveBrand = createBrand();
        inactiveBrand.updateName("비활성 브랜드");
        inactiveBrand.updateStatus(Status.INACTIVE);

        Product product = createProduct(activeBrand, createCategory());
        
        // TestFixture에서 설정된 ID(1L) 사용
        Long productId = 1L;
        Long inactiveBrandId = 1L;
        
        inactiveBrand.updateStatus(Status.INACTIVE);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(brandRepository.findById(inactiveBrandId)).willReturn(Optional.of(inactiveBrand));
        given(categoryRepository.findById(request.getCategoryId())).willReturn(Optional.of(activeCategory));

        // when & then
        assertThatThrownBy(() -> productService.update(productId, request))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.INACTIVE_BRAND.getMessage());
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteSuccess() {
        // given
        Long productId = 1L;
        Product product = createProduct(createBrand(), createCategory());
        
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.delete(productId);

        // then
        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회시 예외 발생")
    void findByIdFail() {
        // given
        Long productId = 999L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.findById(productId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("비활성화된 브랜드로 상품 생성 시 예외 발생")
    void createProductWithInactiveBrandFail() {
        // given
        ProductDto.Request request = createProductRequest();
        Brand brand = createBrand();
        brand.delete();  // 브랜드 비활성화
        Category category = createCategory();

        given(brandRepository.findById(request.getBrandId())).willReturn(Optional.of(brand));
        given(categoryRepository.findById(request.getCategoryId())).willReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() -> productService.create(request))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.INACTIVE_BRAND.getMessage());
    }

    @Test
    @DisplayName("비활성화된 카테고리로 상품 생성 시 예외 발생")
    void createProductWithInactiveCategoryFail() {
        // given
        ProductDto.Request request = createProductRequest();
        Brand brand = createBrand();
        Category category = createCategory();
        category.delete();  // 카테고리 비활성화

        given(brandRepository.findById(request.getBrandId())).willReturn(Optional.of(brand));
        given(categoryRepository.findById(request.getCategoryId())).willReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() -> productService.create(request))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.INACTIVE_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("브랜드가 삭제된 상품 조회시 브랜드 정보가 null로 반환됨")
    void findProductWithDeletedBrand() {
        // given
        Brand brand = createBrand();
        Category category = createCategory();
        Product product = createProduct(brand, category);
        
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        product.onBrandDeleted();  // 브랜드 삭제 처리

        // when
        ProductDto.Response response = productService.findById(1L);

        // then
        assertThat(response.getBrandId()).isNull();
        assertThat(response.getBrandName()).isNull();
    }

    @Test
    @DisplayName("카테고리가 삭제된 상품 조회시 카테고리 정보가 null로 반환됨")
    void findProductWithDeletedCategory() {
        // given
        Brand brand = createBrand();
        Category category = createCategory();
        Product product = createProduct(brand, category);
        
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        product.onCategoryDeleted();  // 카테고리 삭제 처리

        // when
        ProductDto.Response response = productService.findById(1L);

        // then
        assertThat(response.getCategoryId()).isNull();
        assertThat(response.getCategoryName()).isNull();
    }
}