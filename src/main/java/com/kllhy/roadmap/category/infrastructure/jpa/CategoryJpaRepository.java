package com.kllhy.roadmap.category.infrastructure.jpa;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByTypeAscNameAsc();

    List<Category> findAllByTypeOrderByNameAsc(Type type);

    Optional<Category> findByName(String name);
}
