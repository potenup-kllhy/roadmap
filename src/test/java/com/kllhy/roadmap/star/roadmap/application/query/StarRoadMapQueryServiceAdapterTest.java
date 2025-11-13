package com.kllhy.roadmap.star.roadmap.application.query;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarRoadMapQueryServiceAdapterTest {

    @InjectMocks private StarRoadMapQueryServiceAdapter starRoadMapQueryServiceAdapter;

    @Mock private StarRoadMapRepository starRoadMapRepository;

    @Test
    @DisplayName("ID로 별점 조회 시, 존재하는 경우 StarRoadMapView를 반환한다.")
    void shouldReturnView_whenGetByIdIsCalledWithExistingId() {
        // given
        Long starId = 1L;
        StarRoadMap mockStarRoadMap = mock(StarRoadMap.class);
        when(mockStarRoadMap.getUserId()).thenReturn(10L);
        when(mockStarRoadMap.getRoadMapId()).thenReturn(100L);
        when(mockStarRoadMap.getValue()).thenReturn(5);

        when(starRoadMapRepository.findById(starId)).thenReturn(Optional.of(mockStarRoadMap));

        // when
        var result = starRoadMapQueryServiceAdapter.getById(starId);

        // then
        assertAll(
                () -> assertEquals(10L, result.userId()),
                () -> assertEquals(100L, result.roadMapId()),
                () -> assertEquals(5, result.value()));
    }

    @Test
    @DisplayName("ID로 별점 조회 시, 존재하지 않는 경우 예외를 발생시킨다.")
    void shouldThrowException_whenGetByIdIsCalledWithNonExistingId() {
        // given
        Long starId = 1L;
        when(starRoadMapRepository.findById(starId)).thenReturn(Optional.empty());

        // when & then
        DomainException exception =
                assertThrows(
                        DomainException.class,
                        () -> starRoadMapQueryServiceAdapter.getById(starId));
        assertEquals(StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 ID로 모든 별점 조회 시, 결과를 StarRoadMapView 리스트로 반환한다.")
    void shouldReturnViewList_whenGetAllByUserIdIsCalled() {
        // given
        Long userId = 10L;
        StarRoadMap mockStarRoadMap1 = mock(StarRoadMap.class);
        StarRoadMap mockStarRoadMap2 = mock(StarRoadMap.class);
        when(starRoadMapRepository.findByUserId(userId))
                .thenReturn(List.of(mockStarRoadMap1, mockStarRoadMap2));

        // when
        var results = starRoadMapQueryServiceAdapter.getAllStarByUserId(userId);

        // then
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("유저 ID로 모든 별점 조회 시, 결과가 없으면 빈 리스트를 반환한다.")
    void shouldReturnEmptyList_whenGetAllByUserIdIsCalledWithNoResults() {
        // given
        Long userId = 10L;
        when(starRoadMapRepository.findByUserId(userId)).thenReturn(List.of());

        // when
        var results = starRoadMapQueryServiceAdapter.getAllStarByUserId(userId);

        // then
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("로드맵 ID로 모든 별점 조회 시, 결과를 StarRoadMapView 리스트로 반환한다.")
    void shouldReturnViewList_whenGetAllByRoadmapIdIsCalled() {
        // given
        Long roadmapId = 100L;
        StarRoadMap mockStarRoadMap1 = mock(StarRoadMap.class);
        StarRoadMap mockStarRoadMap2 = mock(StarRoadMap.class);
        when(starRoadMapRepository.findByRoadmapId(roadmapId))
                .thenReturn(List.of(mockStarRoadMap1, mockStarRoadMap2));

        // when
        var results = starRoadMapQueryServiceAdapter.getAllStarByRoadMapId(roadmapId);

        // then
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("로드맵 ID로 모든 별점 조회 시, 결과가 없으면 빈 리스트를 반환한다.")
    void shouldReturnEmptyList_whenGetAllByRoadmapIdIsCalledWithNoResults() {
        // given
        Long roadmapId = 100L;
        when(starRoadMapRepository.findByRoadmapId(roadmapId)).thenReturn(List.of());

        // when
        var results = starRoadMapQueryServiceAdapter.getAllStarByRoadMapId(roadmapId);

        // then
        assertTrue(results.isEmpty());
    }
}
