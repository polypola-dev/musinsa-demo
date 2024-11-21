package com.musinsa.demo.dto.coordination;

import com.musinsa.demo.entity.Category;
import com.musinsa.demo.entity.Product;
import com.musinsa.demo.util.Formatters;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
public class CategoryPriceInfoDto {
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String categoryName;
        private List<BrandPrice> lowestPrice;
        private List<BrandPrice> highestPrice;

        public static Response from(Category category, Optional<Product> lowestPriceProduct, Optional<Product> highestPriceProduct) {
            return new Response(
                    category.getName(),
                    lowestPriceProduct.map(BrandPrice::of)
                            .map(List::of)
                            .orElse(Collections.emptyList()),
                    highestPriceProduct.map(BrandPrice::of)
                            .map(List::of)
                            .orElse(Collections.emptyList())
            );
        }
    }

    @Getter
    @AllArgsConstructor
    private static class BrandPrice {
        private String brandName;
        private String price;

        public static BrandPrice of(Product product) {
            return new BrandPrice(product.getBrandName(), Formatters.PRICE_FORMAT.format(product.getPrice()));
        }
    }
}
