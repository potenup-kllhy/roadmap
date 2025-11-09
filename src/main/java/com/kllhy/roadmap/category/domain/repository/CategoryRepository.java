package com.kllhy.roadmap.category.domain.repository;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.model.Category;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByType(Type type);

    Optional<Category> findByName(String name);
}
