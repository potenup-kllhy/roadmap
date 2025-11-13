package com.kllhy.roadmap.star.roadmap.application.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.application.query.dto.TopicView;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.DeleteStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapCreationService;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapDeletionService;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapUpdateService;
import com.kllhy.roadmap.user.domain.User;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarRoadMapCommandServiceAdapterTest {

    @InjectMocks
    private StarRoadMapCommandServiceAdapter starRoadMapCommandServiceAdapter;

    @Mock
    private StarRoadMapRepository starRoadMapRepository;
    @Mock
    private StarRoadMapCreationService starRoadMapCreationService;
    @Mock
    private StarRoadMapUpdateService starRoadMapUpdateService;
    @Mock
    private StarRoadMapDeletionService starRoadMapDeletionService;
    @Mock
    private UserService userService;
    @Mock
    private RoadMapQueryService roadMapQueryService;
    @Mock
    private User mockUser;

    private UserView mockUserView;
    private RoadMapView mockRoadMapView;

    @BeforeEach
    void setUp() {
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn("testUser@example.com");
        when(mockUser.getStatus()).thenReturn(AccountStatus.ACTIVE);
        when(mockUser.getLeftAt()).thenReturn(null);
        mockUserView = UserView.of(mockUser);

        Timestamp now = Timestamp.from(Instant.now());
        mockRoadMapView =
                new RoadMapView(
                        1L, "title", "desc", null, now, now, false, false, 1L, List.of(mock(TopicView.class)));
    }

    @Test
    @DisplayName("생성 메서드 호출 시 로직을 정상적으로 오케스트레이션한다.")
    void shouldOrchestrateCreation_whenCreateIsCalled() {
        // given
        Long userId = 1L;
        Long roadmapId = 1L;
        int value = 5;

        StarRoadMap mockStarRoadMap = mock(StarRoadMap.class);
        StarRoadMap savedMockStarRoadMap = mock(StarRoadMap.class);

        when(userService.getByUser(userId)).thenReturn(mockUserView);
        when(roadMapQueryService.findById(roadmapId)).thenReturn(mockRoadMapView);
        when(starRoadMapCreationService.create(any(CreateStarRoadMapCommand.class)))
                .thenReturn(mockStarRoadMap);
        when(starRoadMapRepository.save(mockStarRoadMap)).thenReturn(savedMockStarRoadMap);
        when(savedMockStarRoadMap.getId()).thenReturn(99L);

        // when
        Long resultId = starRoadMapCommandServiceAdapter.create(userId, roadmapId, value);

        // then
        assertEquals(99L, resultId);
        verify(starRoadMapCreationService, times(1)).create(any(CreateStarRoadMapCommand.class));
        verify(starRoadMapRepository, times(1)).save(mockStarRoadMap);
    }

    @Test
    @DisplayName("수정 메서드 호출 시 로직을 정상적으로 오케스트레이션한다.")
    void shouldOrchestrateUpdate_whenUpdateIsCalled() {
        // given
        Long userId = 1L;
        Long starRoadmapId = 99L;
        int value = 4;
        Long roadmapId = 1L;

        StarRoadMap mockStarRoadMap = mock(StarRoadMap.class);

        when(starRoadMapRepository.findById(starRoadmapId)).thenReturn(Optional.of(mockStarRoadMap));
        when(mockStarRoadMap.getRoadMapId()).thenReturn(roadmapId);
        when(userService.getByUser(userId)).thenReturn(mockUserView);
        when(roadMapQueryService.findById(roadmapId)).thenReturn(mockRoadMapView);
        doNothing().when(starRoadMapUpdateService).update(any(StarRoadMap.class), any(UpdateStarRoadMapCommand.class));
        when(starRoadMapRepository.save(mockStarRoadMap)).thenReturn(mockStarRoadMap);

        // when
        starRoadMapCommandServiceAdapter.update(starRoadmapId, userId, value);

        // then
        verify(starRoadMapRepository, times(1)).findById(starRoadmapId);
        verify(starRoadMapUpdateService, times(1)).update(any(StarRoadMap.class), any(UpdateStarRoadMapCommand.class));
        verify(starRoadMapRepository, times(1)).save(mockStarRoadMap);
    }

    @Test
    @DisplayName("수정 메서드 호출 시 별점이 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenUpdateWithNonExistentStar() {
        // given
        Long userId = 1L;
        Long starRoadmapId = 99L;
        int value = 4;

        when(starRoadMapRepository.findById(starRoadmapId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(
                DomainException.class,
                () -> starRoadMapCommandServiceAdapter.update(starRoadmapId, userId, value));
        verify(starRoadMapUpdateService, times(0)).update(any(), any());
    }

    @Test
    @DisplayName("삭제 메서드 호출 시 로직을 정상적으로 오케스트레이션한다.")
    void shouldOrchestrateDeletion_whenDeleteIsCalled() {
        // given
        Long userId = 1L;
        Long roadmapId = 1L;

        when(userService.getByUser(userId)).thenReturn(mockUserView);
        when(roadMapQueryService.findById(roadmapId)).thenReturn(mockRoadMapView);
        doNothing()
                .when(starRoadMapDeletionService)
                .deleteByUserIdAndRoadmapId(any(DeleteStarRoadMapCommand.class));

        // when
        starRoadMapCommandServiceAdapter.deleteByUserIdAndRoadmapId(userId, roadmapId);

        // then
        verify(starRoadMapDeletionService, times(1))
                .deleteByUserIdAndRoadmapId(any(DeleteStarRoadMapCommand.class));
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
}
