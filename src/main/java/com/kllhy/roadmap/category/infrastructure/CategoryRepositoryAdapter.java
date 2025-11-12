package com.kllhy.roadmap.category.infrastructure;

import com.kllhy.roadmap.category.domain.enums.CategoryType;
import com.kllhy.roadmap.category.domain.model.Category;
import com.kllhy.roadmap.category.domain.repository.CategoryRepository;
import com.kllhy.roadmap.category.infrastructure.jpa.CategoryJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> findAllByOrderByCategoryTypeAscNameAsc() {
        return categoryJpaRepository.findAllByOrderByCategoryTypeAscNameAsc();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryJpaRepository.existsById(id);
    }

    @Override
    public List<Category> findAllByCategoryTypeOrderByNameAsc(CategoryType categoryType) {
        return categoryJpaRepository.findAllByCategoryTypeOrderByNameAsc(categoryType);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }
}
