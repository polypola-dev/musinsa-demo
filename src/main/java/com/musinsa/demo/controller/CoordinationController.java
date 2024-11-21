package com.musinsa.demo.controller;

import com.musinsa.demo.dto.coordination.BrandsLowestPriceDto;
import com.musinsa.demo.dto.coordination.CategoriesLowestPriceDto;
import com.musinsa.demo.dto.coordination.CategoryPriceInfoDto;
import com.musinsa.demo.service.CoordinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coordination")
public class CoordinationController {
    private final CoordinationService coordinationService;

    // 구현 1) - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
    @GetMapping("/categories/lowest-prices")
    public ResponseEntity<CategoriesLowestPriceDto.Response> getCategoriesLowestPriceProducts() {
        return ResponseEntity.ok(coordinationService.findCategoriesLowestPriceProducts());
    }

    // 구현 2) - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
    @GetMapping("/brands/lowest-prices")
    public ResponseEntity<BrandsLowestPriceDto.Response> getBrandsLowestPriceProducts() {
        return ResponseEntity.ok(coordinationService.findBrandsLowestPriceProducts());
    }

    // 구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
    @GetMapping("/categories/{categoryName}/price-info")
    public ResponseEntity<CategoryPriceInfoDto.Response> getLowestAndHighestPrice(
            @PathVariable String categoryName) {
        return ResponseEntity.ok(coordinationService.findLowestAndHighestPrice(categoryName));
    }

} 