package com.musinsa.demo.dto.coordination;

import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.util.Formatters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BrandsLowestPriceDto {
    @Getter
    @AllArgsConstructor
    public static class Response {
        private LowestPrice lowestPrice;

        public static BrandsLowestPriceDto.Response from(Brand brand, List<Product> products, int lowestTotalPrice) {
            List<CategoryPrice> categoryPrices = products.stream()
                    .map(p -> CategoryPrice.builder()
                            .categoryName(p.getCategory().getName())
                            .price(Formatters.PRICE_FORMAT.format(p.getPrice()))
                            .build()
                    ).collect(Collectors.toList());

            LowestPrice lowestPrice = LowestPrice.builder()
                    .brandName(brand.getName())
                    .categories(categoryPrices)
                    .totalPrice(Formatters.PRICE_FORMAT.format(lowestTotalPrice))
                    .build();
            return new BrandsLowestPriceDto.Response(lowestPrice);
        }

        @Getter
        @Builder
        public static class LowestPrice {
            private String brandName;
            private List<CategoryPrice> categories;
            private String totalPrice;
        }

        @Getter
        @Builder
        public static class CategoryPrice {
            private String categoryName;
            private String price;
        }
    }
}