package com.kllhy.roadmap.category.application.query;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.kllhy.roadmap.category.application.query.dto.CategoryView;
import com.kllhy.roadmap.category.domain.enums.CategoryType;
import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.category.domain.model.Category;
import com.kllhy.roadmap.category.domain.repository.CategoryRepository;
import com.kllhy.roadmap.common.exception.DomainException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceAdapterTest {

    private CategoryQueryServiceAdapter categoryQueryServiceAdapter;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryQueryServiceAdapter = new CategoryQueryServiceAdapter(categoryRepository);
    }

    private Category createMockCategory(Long id, CategoryType type, String name) {
        Category mockCategory = Mockito.mock(Category.class);
        when(mockCategory.getId()).thenReturn(id);
        when(mockCategory.getCategoryType()).thenReturn(type);
        when(mockCategory.getName()).thenReturn(name);
        return mockCategory;
    }

    @Nested
    @DisplayName("ID로 카테고리 조회 테스트")
    class getCategoryByIdTests {

        @Test
        @DisplayName("ID로 카테고리 조회 성공 시 CategoryView가 반환된다.")
        void shouldReturnsCategoryView_whenGetCategoryById() {
            // given
            Long categoryId = 1L;
            Category mockCategory = createMockCategory(categoryId, CategoryType.ROLE, "Backend");
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

            // when
            CategoryView foundCategoryView = categoryQueryServiceAdapter.getCategoryById(categoryId);

            // then
            assertAll(
                    () -> assertNotNull(foundCategoryView),
                    () -> assertEquals(mockCategory.getId(), foundCategoryView.id()),
                    () -> assertEquals(mockCategory.getCategoryType().name(), foundCategoryView.type()),
                    () -> assertEquals(mockCategory.getName(), foundCategoryView.name())
            );
        }

        @Test
        @DisplayName("ID로 카테고리 조회 실패 시 예외가 발생한다.")
        void shouldThrowException_whenGetCategoryById() {
            // given
            Long categoryId = 1L;
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

            // when & then
            DomainException exception = assertThrows(DomainException.class,
                    () -> categoryQueryServiceAdapter.getCategoryById(categoryId)
            );
            assertEquals(CategoryErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("이름으로 카테고리 조회 테스트")
    class getCategoryByNameTests {

        @Test
        @DisplayName("이름으로 카테고리 조회 성공 시 CategoryView가 반환된다.")
        void shouldReturnsCategoryView_whenGetCategoryByName() {
            // given
            String categoryName = "Java";
            Category mockCategory = createMockCategory(2L, CategoryType.SKILL, categoryName);
            when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(mockCategory));

            // when
            CategoryView foundCategoryView = categoryQueryServiceAdapter.getCategoryByName(categoryName);

            // then
            assertAll(
                    () -> assertNotNull(foundCategoryView),
                    () -> assertEquals(mockCategory.getId(), foundCategoryView.id()),
                    () -> assertEquals(mockCategory.getCategoryType().name(), foundCategoryView.type()),
                    () -> assertEquals(mockCategory.getName(), foundCategoryView.name())
            );
        }

        @Test
        @DisplayName("이름으로 카테고리 조회 실패 시 예외가 발생한다.")
        void shouldThrowException_whenGetCategoryByName() {
            // given
            String categoryName = "NonExistent";
            when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

            // when & then
            DomainException exception = assertThrows(DomainException.class, () -> {
                categoryQueryServiceAdapter.getCategoryByName(categoryName);
            });
            assertEquals(CategoryErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("카테고리 전체 조회 테스트")
    class getAllCategoriesOrderedTests {

        @Test
        @DisplayName("모든 카테고리 조회 성공 시 정렬된 CategoryView 목록을 반환한다.")
        void shouldReturnsSortedCategoryViewList_whenGetAllCategoriesOrdered() {
            // given
            Category mockCategory1 = createMockCategory(1L, CategoryType.ROLE, "Backend");
            Category mockCategory2 = createMockCategory(2L, CategoryType.SKILL, "Java");
            List<Category> mockList = List.of(mockCategory1, mockCategory2);
            when(categoryRepository.findAllByOrderByCategoryTypeAscNameAsc()).thenReturn(mockList);

            // when
            List<CategoryView> resultList = categoryQueryServiceAdapter.getAllCategoriesOrdered();

            // then
            assertAll(
                    () -> assertFalse(resultList.isEmpty()),
                    () -> assertEquals(2, resultList.size()),
                    () -> assertEquals(mockCategory1.getId(), resultList.get(0).id()),
                    () -> assertEquals(mockCategory2.getId(), resultList.get(1).id())
            );
        }

        @Test
        @DisplayName("타입으로 카테고리 목록 조회 성공 시 CategoryView 목록 반환")
        void shouldReturnsCategoryViewList_whenGetCategoriesByType() {
            // given
            String typeStr = "SKILL";
            Category mockCategory = createMockCategory(3L, CategoryType.SKILL, "Python");
            List<Category> mockList = Collections.singletonList(mockCategory);
            when(categoryRepository.findAllByCategoryTypeOrderByNameAsc(CategoryType.SKILL)).thenReturn(mockList);

            // when
            List<CategoryView> resultList = categoryQueryServiceAdapter.getCategoriesByType(typeStr);

            // then
            assertAll(
                    () -> assertFalse(resultList.isEmpty()),
                    () -> assertEquals(1, resultList.size()),
                    () -> assertEquals(mockCategory.getId(), resultList.get(0).id())
            );
        }

        @Test
        @DisplayName("잘못된 타입으로 조회 시 DomainException 발생")
        void getCategoriesByType_InvalidType_ThrowsDomainException() {
            // given
            String invalidTypeStr = "INVALID_TYPE";

            // when & then
            DomainException exception = assertThrows(DomainException.class, () -> {
                categoryQueryServiceAdapter.getCategoriesByType(invalidTypeStr);
            });
            assertEquals(CategoryErrorCode.CATEGORY_TYPE_INVALID, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("카테고리 존재 여부 확인 테스트")
    class CategoryExistsTests {

        @Test
        @DisplayName("카테고리 존재 여부를 확인했을 때 true를 반환한다.")
        void shouldReturnTrue_whenCategoryExists() {
            // given
            Long categoryId = 1L;
            when(categoryRepository.existsById(categoryId)).thenReturn(true);

            // when
            boolean exists = categoryQueryServiceAdapter.categoryExists(categoryId);

            // then
            assertTrue(exists);
        }

        @Test
        @DisplayName("카테고리 존재 여부를 확인했을 때 false를 반환한다.")
        void shouldReturnFalse_whenCategoryExists() {
            // given
            Long categoryId = 99L;
            when(categoryRepository.existsById(categoryId)).thenReturn(false);

            // when
            boolean exists = categoryQueryServiceAdapter.categoryExists(categoryId);

            // then
            assertFalse(exists);
        }
    }
}
