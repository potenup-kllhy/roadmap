package com.kllhy.roadmap.star.roadmap.application.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapCreationService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarRoadMapCommandServiceAdapterTest {

    @InjectMocks private StarRoadMapCommandServiceAdapter starRoadMapCommandServiceAdapter;

    @Mock private StarRoadMapRepository starRoadMapRepository;

    @Mock private StarRoadMapCreationService starRoadMapCreationService;

    @Test
    @DisplayName("생성 메서드 호출 시 로직을 정상적으로 오케스트레이션한다.")
    void shouldOrchestrateCreation_whenCreateIsCalled() {
        // given
        CreateStarRoadMapCommand command = new CreateStarRoadMapCommand(1L, 1L, 5);
        StarRoadMap mockStarRoadMap = mock(StarRoadMap.class);
        StarRoadMap savedMockStarRoadMap = mock(StarRoadMap.class);

        when(starRoadMapCreationService.create(command)).thenReturn(mockStarRoadMap);
        when(starRoadMapRepository.save(mockStarRoadMap)).thenReturn(savedMockStarRoadMap);
        when(savedMockStarRoadMap.getId()).thenReturn(99L);

        // when
        Long resultId = starRoadMapCommandServiceAdapter.create(command);

        // then
        assertEquals(99L, resultId);
        verify(starRoadMapCreationService, times(1)).create(command);
        verify(starRoadMapRepository, times(1)).save(mockStarRoadMap);
    }

    @Test
    @DisplayName("수정 메서드 호출 시 로직을 정상적으로 오케스트레이션한다.")
    void shouldOrchestrateUpdate_whenUpdateIsCalled() {
        // given
        UpdateStarRoadMapCommand command = new UpdateStarRoadMapCommand(1L, 99L, 4);
        StarRoadMap mockStarRoadMap = mock(StarRoadMap.class);

        when(starRoadMapRepository.findById(command.starRoadMapId()))
                .thenReturn(Optional.of(mockStarRoadMap));
        when(mockStarRoadMap.getUserId()).thenReturn(command.userId());

        // when
        starRoadMapCommandServiceAdapter.update(command);

        // then
        verify(mockStarRoadMap, times(1)).update(command.value());
    }

    @Test
    @DisplayName("소유자가 아닌 사용자가 수정 메서드 호출 시 예외가 발생한다.")
    void shouldThrowException_whenUpdateByNonOwner() {
        // given
        UpdateStarRoadMapCommand command = new UpdateStarRoadMapCommand(1L, 99L, 4);
        StarRoadMap mockStarRoadMap = mock(StarRoadMap.class);

        when(starRoadMapRepository.findById(command.starRoadMapId()))
                .thenReturn(Optional.of(mockStarRoadMap));
        when(mockStarRoadMap.getUserId()).thenReturn(2L);

        // when & then
        DomainException exception =
                assertThrows(
                        DomainException.class,
                        () -> starRoadMapCommandServiceAdapter.update(command));
        assertEquals(StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_AUTHORIZED, exception.getErrorCode());
        verify(mockStarRoadMap, never()).update(anyInt());
    }

    @Test
    @DisplayName("별점 아이디로 삭제 메서드 호출 시 리포지토리에 정상적으로 위임한다.")
    void shouldDelegateDeleteById_whenDeleteByIdIsCalled() {
        // given
        Long starId = 99L;
        doNothing().when(starRoadMapRepository).deleteById(starId);

        // when
        starRoadMapCommandServiceAdapter.deleteById(starId);

        // then
        verify(starRoadMapRepository, times(1)).deleteById(starId);
    }

    @Test
    @DisplayName("유저 아이디에 해당하는 모든 별점 삭제 메서드를 호출 시 리포지토리에 정상적으로 위임한다.")
    void shouldDelegateDeleteAllByUserId_whenDeleteAllByUserIdIsCalled() {
        // given
        Long userId = 1L;
        doNothing().when(starRoadMapRepository).deleteAllByUserId(userId);

        // when
        starRoadMapCommandServiceAdapter.deleteAllStarByUserId(userId);

        // then
        verify(starRoadMapRepository, times(1)).deleteAllByUserId(userId);
    }

    @Test
    @DisplayName("로드맵 아이디에 해당하는 모든 별점 삭제 메서드 호출 시 리포지토리에 정상적으로 위임한다.")
    void shouldDelegateDeleteAllByRoadMapId_whenDeleteAllByRoadMapIdIsCalled() {
        // given
        Long roadmapId = 1L;
        doNothing().when(starRoadMapRepository).deleteAllByRoadmapId(roadmapId);

        // when
        starRoadMapCommandServiceAdapter.deleteAllStarByRoadMapId(roadmapId);

        // then
        verify(starRoadMapRepository, times(1)).deleteAllByRoadmapId(roadmapId);
    }

    @Test
    @DisplayName("유저 아이디와 로드맵에 해당하는 별점 삭제 메서드 호출 시 리포지토리에 정상적으로 위임한다.")
    void shouldDelegateDeleteByUserIdAndRoadmapId_whenDeleteByUserIdAndRoadmapIdIsCalled() {
        // given
        Long userId = 1L;
        Long roadmapId = 1L;
        doNothing().when(starRoadMapRepository).deleteByUserIdAndRoadmapId(userId, roadmapId);

        // when
        starRoadMapCommandServiceAdapter.deleteByUserIdAndRoadmapId(userId, roadmapId);

        // then
        verify(starRoadMapRepository, times(1)).deleteByUserIdAndRoadmapId(userId, roadmapId);
    }
}
