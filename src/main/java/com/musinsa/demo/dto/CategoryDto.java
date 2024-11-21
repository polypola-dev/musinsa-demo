package com.musinsa.demo.dto;

import com.musinsa.demo.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "카테고리명은 필수입니다")
        private String name;
    }
    
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        
        public static Response from(Category category) {
            return new Response(category.getId(), category.getName());
        }
    }
} 