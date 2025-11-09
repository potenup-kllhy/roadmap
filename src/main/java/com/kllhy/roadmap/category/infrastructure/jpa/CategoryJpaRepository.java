package com.kllhy.roadmap.category.infrastructure.jpa;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByType(Type type);

    Optional<Category> findByName(String name);
}
