package com.musinsa.demo.dto;

import com.musinsa.demo.entity.Product;
import com.musinsa.demo.entity.status.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ProductDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "상품명은 필수입니다")
        private String name;

        @NotNull(message = "가격은 필수입니다")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
        private Integer price;

        @NotNull(message = "브랜드 ID는 필수입니다")
        private Long brandId;

        @NotNull(message = "카테고리 ID는 필수입니다")
        private Long categoryId;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private int price;
        private Long brandId;
        private String brandName;
        private Long categoryId;
        private String categoryName;
        private ProductStatus status;

        public static Response from(Product product) {
            return new Response(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getBrandId(),
                    product.getBrandName(),
                    product.getCategoryId(),
                    product.getCategoryName(),
                    product.getStatus()
            );
        }
    }
} 