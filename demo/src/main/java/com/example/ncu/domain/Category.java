package com.example.ncu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "category", schema = "ncu")
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "category_id", nullable = false)
    private int categoryId;
    @Basic
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    @Basic
    @Column(name = "career_id_category", nullable = false)
    private int careerIdCategory;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "career_id_category", referencedColumnName = "career_id", nullable = false, insertable = false, updatable = false)
    private Career careerByCareerIdCategory;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCareerIdCategory() {
        return careerIdCategory;
    }

    public void setCareerIdCategory(int careerIdCategory) { this.careerIdCategory = careerIdCategory; }
}

