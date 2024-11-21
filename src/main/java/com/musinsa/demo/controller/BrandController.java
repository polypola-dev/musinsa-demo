package com.musinsa.demo.controller;

import com.musinsa.demo.dto.BrandDto;
import com.musinsa.demo.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandDto.Response> createBrand(@Valid @RequestBody BrandDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(brandService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDto.Response> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandDto.Request request) {
        return ResponseEntity.ok(brandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto.Response> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<BrandDto.Response>> getBrandAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

} 