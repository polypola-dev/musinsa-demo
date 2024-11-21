package com.musinsa.demo.entity;

import com.musinsa.demo.entity.status.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status != 'INACTIVE'")
public class Product extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private int price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Builder
    public Product(String name, int price, ProductStatus status, Brand brand, Category category) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.status = status != null ? status : ProductStatus.ACTIVE;
    }

    public void update(String name, int price, Brand brand, Category category) {
        updateName(name);
        updatePrice(price);
        updateBrand(brand);
        updateCategory(category);
    }

    public void updateName(String name) {
        this.name = name;
    }
    
    public void updatePrice(int price) {
        this.price = price;
    }
    
    public void updateBrand(Brand brand) {
        this.brand = brand;
    }
    
    public void updateCategory(Category category) {
        this.category = category;
    }
    
    public void delete() {
        this.status = ProductStatus.INACTIVE;
    }
    
    public void updateStatus(ProductStatus status) {
        this.status = status;
    }
    
    public void onBrandDeleted() {
        this.brand = null;
    }
    
    public void onCategoryDeleted() {
        this.category = null;
    }
    
    public String getBrandName() {
        return brand != null ? brand.getName() : null;
    }
    
    public Long getBrandId() {
        return brand != null ? brand.getId() : null;
    }
    
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }
    
    public Long getCategoryId() {
        return category != null ? category.getId() : null;
    }
} 