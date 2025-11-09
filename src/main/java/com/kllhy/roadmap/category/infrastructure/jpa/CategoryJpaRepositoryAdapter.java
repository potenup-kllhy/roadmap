package com.kllhy.roadmap.category.infrastructure.jpa;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.model.Category;
import com.kllhy.roadmap.category.domain.repository.CategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryJpaRepositoryAdapter implements CategoryRepository {

    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public Optional<Category> findByType(Type type) {
        return categoryJpaRepository.findByType(type);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }
}
