package com.kllhy.roadmap.category.application.query;

import com.kllhy.roadmap.category.application.query.dto.CategoryView;
import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.category.domain.model.Category;
import com.kllhy.roadmap.category.domain.repository.CategoryRepository;
import com.kllhy.roadmap.common.exception.DomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryServiceAdapter implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryView> getAllCategoriesOrdered() {
        return categoryRepository.findAllByOrderByTypeAscNameAsc().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    public CategoryView getCategoryById(Long id) {
        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new DomainException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return toView(category);
    }

    @Override
    public CategoryView getCategoryByName(String name) {
        Category category =
                categoryRepository
                        .findByName(name)
                        .orElseThrow(
                                () -> new DomainException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return toView(category);
    }

    @Override
    public List<CategoryView> getCategoriesByType(String type) {
        return categoryRepository.findAllByTypeOrderByNameAsc(Type.from(type)).stream()
                .map(this::toView)
                .toList();
    }

    @Override
    public boolean categoryExists(Long id) {
        return categoryRepository.existsById(id);
    }

    private CategoryView toView(Category category) {
        return new CategoryView(category.getId(), category.getType().name(), category.getName());
    }
}
