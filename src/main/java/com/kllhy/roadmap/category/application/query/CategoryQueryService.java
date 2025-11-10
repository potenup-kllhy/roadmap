package com.kllhy.roadmap.category.application.query;

import com.kllhy.roadmap.category.application.query.dto.CategoryView;

import java.util.List;

public interface CategoryQueryService {
    List<CategoryView> getAllCategoriesOrdered();

    CategoryView getCategoryById(Long id);

    CategoryView getCategoryByName(String name);

    List<CategoryView> getCategoriesByType(String type);

    boolean categoryExists(Long id);
}