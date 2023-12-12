package com.example.ncu.controller;


import com.example.ncu.domain.Category;
import com.example.ncu.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService service;

    public CategoryController(CategoryService service) {
        super();
        this.service = service;
    }

    // build get all models REST API
    @GetMapping("/findAll/{careerId}")
    public List<Category> getAllCategorys(@PathVariable("careerId") Integer career_id){
        return service.getAllCategorys(career_id);
    }
}

