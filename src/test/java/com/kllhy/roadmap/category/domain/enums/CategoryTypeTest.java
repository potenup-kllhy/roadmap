package com.kllhy.roadmap.category.domain.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CategoryTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"role", "ROLE", "Role"})
    @DisplayName("다양한 ROLE 문자열 입력에 대해 Type.ROLE을 반환한다.")
    void shouldReturnRole_forVariousRoleStrings(String input) {
        // when
        CategoryType result = CategoryType.from(input);

        // then
        assertEquals(CategoryType.ROLE, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"skill", "SKILL", "Skill"})
    @DisplayName("다양한 SKILL 문자열 입력에 대해 Type.SKILL을 반환한다.")
    void shouldReturnSkill_forVariousSkillStrings(String input) {
        // when
        CategoryType result = CategoryType.from(input);

        // then
        assertEquals(CategoryType.SKILL, result);
    }

    @Test
    @DisplayName("유효하지 않은 문자열 입력에 대해 예외가 발생한다.")
    void shouldThrowDomainException_forInvalidString() {
        // given
        String invalidInput = "INVALID_TYPE";

        // when & then
        DomainException exception =
                assertThrows(DomainException.class, () -> CategoryType.from(invalidInput));
        assertEquals(CategoryErrorCode.CATEGORY_TYPE_INVALID, exception.getErrorCode());
    }
}
