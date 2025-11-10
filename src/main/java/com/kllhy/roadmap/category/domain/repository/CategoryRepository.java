package com.kllhy.roadmap.category.domain.repository;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAllByOrderByTypeAscNameAsc();

    Optional<Category> findById(Long id);

    boolean existsById(Long id);

    List<Category> findAllByType(Type type);

    Optional<Category> findByName(String name);
}
