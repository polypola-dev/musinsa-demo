package com.musinsa.demo.service;

import com.musinsa.demo.config.error.ErrorCode;
import com.musinsa.demo.config.error.exception.DuplicateException;
import com.musinsa.demo.config.error.exception.NotFoundException;
import com.musinsa.demo.dto.BrandDto;
import com.musinsa.demo.entity.Brand;
import com.musinsa.demo.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public BrandDto.Response create(BrandDto.Request request) {
        validateDuplicateBrandName(request.getName());

        Brand brand = Brand.builder()
                .name(request.getName())
                .build();
        return BrandDto.Response.from(brandRepository.save(brand));
    }

    @Transactional
    public BrandDto.Response update(Long id, BrandDto.Request request) {
        validateDuplicateBrandNameExceptSelf(request.getName(), id);

        Brand brand = findBrandById(id);
        brand.updateName(request.getName());
        return BrandDto.Response.from(brand);
    }

    @Transactional
    public void delete(Long id) {
        Brand brand = findBrandById(id);
        brand.delete();
    }

    public BrandDto.Response findById(Long id) {
        return BrandDto.Response.from(findBrandById(id));
    }

    public List<BrandDto.Response> findAll() {
        return brandRepository.findAll().stream()
                .map(BrandDto.Response::from)
                .collect(Collectors.toList());
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));
    }

    private void validateDuplicateBrandName(String name) {
        if (brandRepository.existsByName(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_BRAND_NAME);
        }
    }

    private void validateDuplicateBrandNameExceptSelf(String name, Long id) {
        if (brandRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_BRAND_NAME);
        }
    }
} 