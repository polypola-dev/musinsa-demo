package com.musinsa.demo.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 오류가 발생했습니다"),

    // Client Error
    INVALID_INPUT_REQUEST(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    INVALID_TYPE_REQUEST(HttpStatus.BAD_REQUEST, "C002", "잘못된 타입이 입력되었습니다"),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "C003", "필수 파라미터가 누락되었습니다"),
    UNSUPPORTED_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "C004", "지원하지 않는 HTTP 메서드입니다"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C005", "지원하지 않는 미디어 타입입니다"),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다"),

    // Brand
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "브랜드를 찾을 수 없습니다"),
    INACTIVE_BRAND(HttpStatus.BAD_REQUEST, "B002", "비활성된 브랜드는 사용할 수 없습니다"),
    DUPLICATE_BRAND_NAME(HttpStatus.BAD_REQUEST, "B003", "이미 존재하는 브랜드명입니다"),

    // Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "카테고리를 찾을 수 없습니다"),
    INACTIVE_CATEGORY(HttpStatus.BAD_REQUEST, "C002", "비활성된 카테고리는 사용할 수 없습니다"),
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "C003", "이미 존재하는 카테고리명입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
} 