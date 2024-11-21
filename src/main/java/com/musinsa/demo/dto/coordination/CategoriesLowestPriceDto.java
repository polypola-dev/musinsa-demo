package com.musinsa.demo.dto.coordination;

import com.musinsa.demo.entity.Product;
import com.musinsa.demo.util.Formatters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoriesLowestPriceDto {
    @Getter
    @AllArgsConstructor
    public static class Response {
        private final List<CategoryPrice> items;
        private final String totalPrice;

        public static CategoriesLowestPriceDto.Response from(List<Product> products) {
            final int[] totalPriceSum = {0};
            List<CategoryPrice> items = products.stream()
                    .map(product -> {
                        totalPriceSum[0] += product.getPrice();
                        return CategoryPrice.builder()
                                .categoryName(product.getCategoryName())
                                .brandName(product.getBrandName())
                                .price(Formatters.PRICE_FORMAT.format(product.getPrice()))
                                .build();
                    })
                    .collect(Collectors.toList());
            return new CategoriesLowestPriceDto.Response(items,
                    Formatters.PRICE_FORMAT.format(totalPriceSum[0]));
        }
    }

    @Getter
    @Builder
    private static class CategoryPrice {
        private final String categoryName;
        private final String brandName;
        private final String price;
    }
}