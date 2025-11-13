package com.kllhy.roadmap.star.roadmap.domain.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StarRoadMapTest {

    @Test
    @DisplayName("정상적인 값으로 StarRoadMap 생성 시 정상적으로 생성된다.")
    void shouldCreateSuccessfully_whenGivenValidCommand() {
        // given
        CreateStarRoadMapCommand command = new CreateStarRoadMapCommand(1L, 1L, 5);

        // when
        StarRoadMap starRoadMap = StarRoadMap.create(command);

        // then
        assertAll(
                () -> assertNotNull(starRoadMap),
                () -> assertEquals(command.userId(), starRoadMap.getUserId()),
                () -> assertEquals(command.roadmapId(), starRoadMap.getRoadMapId()),
                () -> assertEquals(command.value(), starRoadMap.getValue()));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 6})
    @DisplayName("유효하지 않은 값으로 StarRoadMap 생성 시 예외를 던진다.")
    void shouldThrowException_whenCreateWithInvalidValue(int invalidValue) {
        // given
        CreateStarRoadMapCommand command = new CreateStarRoadMapCommand(1L, 1L, invalidValue);

        // when & then
        assertThrows(DomainException.class, () -> StarRoadMap.create(command));
    }

    @Test
    @DisplayName("정상적인 값으로 별점 수정 시 정상적으로 수정된다.")
    void shouldUpdateSuccessfully_whenGivenValidValue() {
        // given
        CreateStarRoadMapCommand command = new CreateStarRoadMapCommand(1L, 1L, 3);
        StarRoadMap starRoadMap = StarRoadMap.create(command);
        int newValue = 5;

        // when
        starRoadMap.update(newValue);

        // then
        assertEquals(newValue, starRoadMap.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 6})
    @DisplayName("유효하지 않은 값으로 별점 수정 시 예외를 던진다.")
    void shouldThrowException_whenUpdateWithInvalidValue(int invalidValue) {
        // given
        CreateStarRoadMapCommand command = new CreateStarRoadMapCommand(1L, 1L, 3);
        StarRoadMap starRoadMap = StarRoadMap.create(command);

        // when & then
        assertThrows(DomainException.class, () -> starRoadMap.update(invalidValue));
    }
}
