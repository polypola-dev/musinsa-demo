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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductDto.Response create(ProductDto.Request request) {
        Brand brand = findBrandById(request.getBrandId());
        Category category = findCategoryById(request.getCategoryId());

        validateBrandAndCategoryStatus(brand, category);

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .brand(brand)
                .category(category)
                .build();

        return ProductDto.Response.from(productRepository.save(product));
    }

    @Transactional
    public ProductDto.Response update(Long id, ProductDto.Request request) {
        Product product = findProductById(id);
        Brand brand = findBrandById(request.getBrandId());
        Category category = findCategoryById(request.getCategoryId());

        validateBrandAndCategoryStatus(brand, category);

        product.update(request.getName(), request.getPrice(), brand, category);

        return ProductDto.Response.from(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(findProductById(id));
    }

    public ProductDto.Response findById(Long id) {
        return ProductDto.Response.from(findProductById(id));
    }

    public List<ProductDto.Response> findAll() {
        return productRepository.findAll().stream()
                .map(ProductDto.Response::from)
                .collect(Collectors.toList());
    }


    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private void validateBrandAndCategoryStatus(Brand brand, Category category) {
        if (brand.getStatus() == Status.INACTIVE) {
            throw new BusinessException(ErrorCode.INACTIVE_BRAND);
        }
        if (category.getStatus() == Status.INACTIVE) {
            throw new BusinessException(ErrorCode.INACTIVE_CATEGORY);
        }
    }
}