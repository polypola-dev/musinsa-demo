package com.musinsa.demo.repository;

import com.musinsa.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findFirstByBrand_IdAndCategory_IdOrderByPriceAscIdDesc(Long brandId, Long categoryId);

    @Query("""
            SELECT p FROM Product p
            JOIN FETCH p.brand b
            JOIN FETCH p.category c
            WHERE c.id = :categoryId
            AND p.status = 'ACTIVE'
            ORDER BY p.price ASC, p.id DESC
            LIMIT 1
            """)
    Optional<Product> findByCategoryLowestPrice(@Param("categoryId") Long categoryId);

    @Query("""
            SELECT p FROM Product p
            JOIN FETCH p.brand b
            JOIN FETCH p.category c
            WHERE c.id = :categoryId
            AND p.status = 'ACTIVE'
            ORDER BY p.price DESC, p.id DESC
            LIMIT 1
            """)
    Optional<Product> findByCategoryHighestPrice(@Param("categoryId") Long categoryId);
}