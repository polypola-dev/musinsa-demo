package com.musinsa.demo.entity;

import com.musinsa.demo.entity.status.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status != 'INACTIVE'")
public class Brand extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "brand")
    private Set<Product> products = new HashSet<>();

    @Builder
    public Brand(String name, Status status) {
        this.name = name;
        this.status = status != null ? status : Status.ACTIVE;
    }

    public void update(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void delete() {
        products.forEach(Product::onBrandDeleted);
        this.status = Status.INACTIVE;
    }
} 