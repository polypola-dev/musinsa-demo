package com.musinsa.demo.entity.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductStatus {
    ACTIVE("판매중"),
    INACTIVE("판매중지"),
    SOLD_OUT("품절");
    
    private final String description;
}