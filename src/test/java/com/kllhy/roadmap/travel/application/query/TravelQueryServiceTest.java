package com.kllhy.roadmap.travel.application.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.travel.application.service.query.TravelQueryServiceAdapter;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class TravelQueryServiceTest {
    @InjectMocks private TravelQueryServiceAdapter service;

    @Mock private TravelRepository travelRepository;

    @Mock private UserService userService;

    private static Travel makeTravel(long userId, long roadmapId, boolean archived) {
        var topics =
                List.of(
                        new ProgressTopicCommand(
                                10L,
                                List.of(
                                        new ProgressSubTopicCommand(101L),
                                        new ProgressSubTopicCommand(102L))),
                        new ProgressTopicCommand(20L, List.of(new ProgressSubTopicCommand(201L))));
        var t = Travel.create(new TravelCommand(userId, roadmapId, topics));
        if (archived) t.activate(ActiveStatus.INACTIVE);
        return t;
    }

    @Test
    @DisplayName("getById: 존재하고 활성 상태면 TravelView 반환")
    void getById_ok() {
        long travelId = 1L;
        var travel = makeTravel(7L, 100L, false);

        when(travelRepository.findBatchById(travelId)).thenReturn(Optional.of(travel));

        TravelView view = service.getById(travelId);

        assertThat(view.userId()).isEqualTo(7L);
        assertThat(view.roadmapId()).isEqualTo(100L);
        assertThat(view.progressTopicViews().size()).isEqualTo(2);
        verify(travelRepository).findBatchById(travelId);
    }

    @Test
    @DisplayName("getById: archived면 DomainException(TRA…_NOT_FOUND)")
    void getById_archived() {
        long travelId = 1L;
        var archived = makeTravel(7L, 100L, true);
        when(travelRepository.findBatchById(travelId)).thenReturn(Optional.of(archived));

        assertThatThrownBy(() -> service.getById(travelId)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("getByUser: ACTIVE 사용자만 조회 가능하며 archived는 필터링")
    void getByUser_ok_filters_archived() {
        long userId = 7L;

        var activeUser = new UserView(userId, null, AccountStatus.ACTIVE, null);
        when(userService.getByUser(userId)).thenReturn(activeUser);

        var t1 = makeTravel(userId, 100L, false);
        var t2 = makeTravel(userId, 200L, true); // archived → 필터링
        var t3 = makeTravel(userId, 300L, false);

        when(travelRepository.findBatchByUserId(eq(userId), eq(Pageable.unpaged())))
                .thenReturn(new PageImpl<>(List.of(t1, t2, t3)));

        List<TravelView> views = service.getByUser(userId);

        assertThat(views).hasSize(2);
        assertThat(views.stream().map(TravelView::roadmapId).toList())
                .containsExactlyInAnyOrder(100L, 300L);

        verify(userService).getByUser(userId);
        verify(travelRepository).findBatchByUserId(eq(userId), eq(Pageable.unpaged()));
    }

    @Test
    @DisplayName("getByUser: 비활성 사용자면 DomainException(권한 없음)")
    void getByUser_inactive_user() {
        long userId = 7L;
        var disabled = new UserView(userId, null, AccountStatus.DISABLED, null);
        when(userService.getByUser(userId)).thenReturn(disabled);

        assertThatThrownBy(() -> service.getByUser(userId)).isInstanceOf(DomainException.class);

        verify(userService).getByUser(userId);
        verifyNoInteractions(travelRepository);
    }
}
