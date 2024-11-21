package com.musinsa.demo.dto;

import com.musinsa.demo.entity.Brand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BrandDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "브랜드명은 필수입니다")
        private String name;
    }
    
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        
        public static Response from(Brand brand) {
            return new Response(brand.getId(), brand.getName());
        }
    }
} 