package com.kllhy.roadmap.category.infrastructure.jpa;

import com.kllhy.roadmap.category.domain.enums.CategoryType;
import com.kllhy.roadmap.category.domain.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByCategoryTypeAscNameAsc();

    List<Category> findAllByCategoryTypeOrderByNameAsc(CategoryType categoryType);

    Optional<Category> findByName(String name);
}
