package com.example.ncu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ncu.domain.Category;

import java.util.List;

@org.springframework.stereotype.Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCareerIdCategory(Integer careerId);

}
