package com.kllhy.roadmap.category.presentation;

import com.kllhy.roadmap.category.application.query.CategoryQueryService;
import com.kllhy.roadmap.category.application.query.dto.CategoryView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping
    public ResponseEntity<List<CategoryView>> getCategories(@RequestParam(required = false) String type) {
        if (type != null) {
            return ResponseEntity.ok().body(categoryQueryService.getCategoriesByType(type));
        }
        return ResponseEntity.ok().body(categoryQueryService.getAllCategoriesOrdered());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryView> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryQueryService.getCategoryById(id));
    }

    @GetMapping("/detail")
    public ResponseEntity<CategoryView> getCategoryByName(@RequestParam String name) {
        return ResponseEntity.ok().body(categoryQueryService.getCategoryByName(name));
    }
}
