package com.kllhy.roadmap.category.domain.service;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.category.domain.model.Category;
import com.kllhy.roadmap.category.domain.repository.CategoryRepository;
import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategoriesOrdered() {
        return categoryRepository.findAllByOrderByTypeAscNameAsc();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new DomainException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    public boolean categoryExists(Long id) {
        return categoryRepository.existsById(id);
    }

    public List<Category> getCategoriesByType(String type) {
        return categoryRepository.findAllByTypeOrderByNameAsc(Type.from(type));
    }

    public Category getCategoryByName(String name) {
        return categoryRepository
                .findByName(name)
                .orElseThrow(() -> new DomainException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }
}
