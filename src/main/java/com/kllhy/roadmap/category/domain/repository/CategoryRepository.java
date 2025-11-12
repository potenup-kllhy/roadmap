package com.kllhy.roadmap.category.domain.repository;

import com.kllhy.roadmap.category.domain.enums.CategoryType;
import com.kllhy.roadmap.category.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAllByOrderByCategoryTypeAscNameAsc();

    Optional<Category> findById(Long id);

    boolean existsById(Long id);

    List<Category> findAllByCategoryTypeOrderByNameAsc(CategoryType categoryType);

    Optional<Category> findByName(String name);
}
