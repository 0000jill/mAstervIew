package com.example.ncu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ncu.domain.Category;
import com.example.ncu.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository cr;


    public List<Category> getAllCategorys(Integer career_id){

        return cr.findByCareerIdCategory(career_id);
    }

}

