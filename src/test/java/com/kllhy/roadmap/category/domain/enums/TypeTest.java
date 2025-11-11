package com.kllhy.roadmap.category.domain.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kllhy.roadmap.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"role", "ROLE", "Role"})
    @DisplayName("다양한 ROLE 문자열 입력에 대해 Type.ROLE을 반환한다.")
    void shouldReturnRole_forVariousRoleStrings(String input) {
        // when
        Type result = Type.from(input);

        // then
        assertEquals(Type.ROLE, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"skill", "SKILL", "Skill"})
    @DisplayName("다양한 SKILL 문자열 입력에 대해 Type.SKILL을 반환한다.")
    void shouldReturnSkill_forVariousSkillStrings(String input) {
        // when
        Type result = Type.from(input);

        // then
        assertEquals(Type.SKILL, result);
    }

    @Test
    @DisplayName("유효하지 않은 문자열 입력에 대해 예외가 발생한다.")
    void shouldThrowDomainException_forInvalidString() {
        // given
        String invalidInput = "INVALID_TYPE";

        // when & then
        assertThrows(DomainException.class, () -> Type.from(invalidInput), "Invalid category type");
    }
}
