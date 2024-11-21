package com.musinsa.demo.controller;

import com.musinsa.demo.service.CategoryService;
import com.musinsa.demo.service.CoordinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/coordination")
public class CoordinationViewController {

    private final CoordinationService coordinationService;
    private final CategoryService categoryService;

    @GetMapping
    public String getCoordinateInfos(Model model) {
        // 카테고리별 코디 최저가
        model.addAttribute("categoryLowestPrices", coordinationService.findCategoriesLowestPriceProducts());
        // 단일 브랜드 코디 최저가
        model.addAttribute("brandLowestPrice", coordinationService.findBrandsLowestPriceProducts());
        // 카테고리 목록
        model.addAttribute("categories", categoryService.findAll());

        return "coordination/list";
    }

} 