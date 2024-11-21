package com.musinsa.demo.service;

import com.musinsa.demo.config.error.ErrorCode;
import com.musinsa.demo.config.error.exception.BusinessException;
import com.musinsa.demo.dto.BrandDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.entity.status.Status;
import com.musinsa.demo.repository.BrandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.musinsa.demo.TestFixture.createBrand;
import static com.musinsa.demo.TestFixture.createCategory;
import static com.musinsa.demo.TestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @Test
    @DisplayName("브랜드 생성 성공")
    void createBrandSuccess() {
        // given
        BrandDto.Request request = BrandDto.Request.builder()
                .name("테스트 브랜드")
                .build();

        Brand brand = Brand.builder()
                .name(request.getName())
                .build();

        given(brandRepository.existsByName(request.getName())).willReturn(false);
        given(brandRepository.save(any(Brand.class))).willReturn(brand);

        // when
        BrandDto.Response response = brandService.create(request);

        // then
        assertThat(response.getName()).isEqualTo(request.getName());
        verify(brandRepository).existsByName(request.getName());
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    @DisplayName("브랜드 생성 시 이름 중복 검증")
    void validateDuplicateBrandNameOnCreate() {
        // given
        BrandDto.Request request = BrandDto.Request.builder()
                .name("테스트 브랜드")
                .build();

        given(brandRepository.existsByName(request.getName())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> brandService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_BRAND_NAME.getMessage());

        verify(brandRepository).existsByName(request.getName());
    }

    @Test
    @DisplayName("브랜드 수정 시 이름 중복 검증")
    void validateDuplicateBrandNameOnUpdateName() {
        // given
        Long brandId = 1L;
        BrandDto.Request request = BrandDto.Request.builder()
                .name("테스트 브랜드")
                .build();

        Brand brand = createBrand();

        given(brandRepository.existsByNameAndIdNot(request.getName(), brandId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> brandService.update(brandId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_BRAND_NAME.getMessage());

        verify(brandRepository).existsByNameAndIdNot(request.getName(), brandId);
    }

    @Test
    @DisplayName("브랜드 삭제시 연관된 상품들의 브랜드 참조가 제거됨")
    void deleteBrandWithProducts() {
        // given
        Brand brand = createBrand();
        Product product = createProduct(brand, createCategory());
        brand.getProducts().add(product);

        given(brandRepository.findById(brand.getId())).willReturn(Optional.of(brand));

        // when
        brandService.delete(brand.getId());

        // then
        assertThat(brand.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(product.getBrand()).isNull();
        verify(brandRepository).findById(brand.getId());
    }
} 