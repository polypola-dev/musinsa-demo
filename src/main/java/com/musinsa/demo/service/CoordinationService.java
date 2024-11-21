package com.musinsa.demo.service;

import com.musinsa.demo.config.error.ErrorCode;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoordinationService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoriesLowestPriceDto.Response findCategoriesLowestPriceProducts() {
        // 전체 카테고리 조회
        List<Category> categories = categoryRepository.findAll();

        // 카테고리별 최저가 상품 조회
        List<Product> lowestPriceProducts = categories.stream()
                .map(category -> productRepository.findByCategoryLowestPrice(category.getId()))
                .flatMap(Optional::stream)
                .sorted(Comparator.comparing(product -> product.getCategoryId()))
                .collect(Collectors.toList());

        return CategoriesLowestPriceDto.Response.from(lowestPriceProducts);
    }

    public BrandsLowestPriceDto.Response findBrandsLowestPriceProducts() {
        // 전체 브랜드 조회
        List<Brand> brands = brandRepository.findAll();
        // 전체 카테고리 조회
        List<Category> categories = categoryRepository.findAll();

        // 브랜드별 모든 카테고리 상품의 총합 최저가 조회
        Brand lowestPriceBrand = null;
        int lowestTotalPrice = Integer.MAX_VALUE;
        List<Product> lowestProducts = Collections.emptyList();

        for (Brand brand : brands) {
            List<Product> products = new ArrayList<>();
            int totalPrice = 0;

            for (Category category : categories) {
                Product product = productRepository.findFirstByBrand_IdAndCategory_IdOrderByPriceAscIdDesc(
                        brand.getId(), category.getId()).get();
                totalPrice += product.getPrice();
                products.add(product);
            }

            if (lowestTotalPrice > totalPrice) {
                lowestTotalPrice = totalPrice;
                lowestPriceBrand = brand;
                lowestProducts = products;
            }
        }
        return BrandsLowestPriceDto.Response.from(lowestPriceBrand, lowestProducts, lowestTotalPrice);

    }

    public CategoryPriceInfoDto.Response findLowestAndHighestPrice(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        Optional<Product> lowestProduct = productRepository.findByCategoryLowestPrice(category.getId());

        Optional<Product> highestProduct = productRepository.findByCategoryHighestPrice(category.getId());

        return CategoryPriceInfoDto.Response.from(category, lowestProduct, highestProduct);
    }
} 