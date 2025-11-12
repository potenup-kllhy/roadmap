package com.kllhy.roadmap.travel.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.application.query.dto.SubTopicView;
import com.kllhy.roadmap.roadmap.application.query.dto.TopicView;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import com.kllhy.roadmap.travel.application.command.TravelServiceAdapter;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import com.kllhy.roadmap.travel.domain.service.TravelCreationService;
import com.kllhy.roadmap.travel.domain.service.TravelUpdateService;
import com.kllhy.roadmap.travel.presentation.request.ProgressSubTopicUpdateRequest;
import com.kllhy.roadmap.travel.presentation.request.ProgressTopicUpdateRequest;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {

    @InjectMocks private TravelServiceAdapter service; // 테스트 대상

    @Mock private TravelRepository travelRepository;
    @Mock private TravelCreationService travelCreationService;
    @Mock private UserService userService;
    @Mock private RoadMapQueryService roadMapQueryService;
    @Mock private TravelUpdateService travelUpdateService;

    // --------- helpers ---------
    private static SubTopicView sub(long id) {
        return new SubTopicView(
                id,
                "sub-" + id,
                "content",
                ImportanceLevel.DEFAULT,
                null,
                false,
                false,
                List.of(),
                null,
                null);
    }

    private static TopicView topic(long id, List<SubTopicView> subs) {
        return new TopicView(
                id,
                List.of(), // resources (null 금지)
                subs, // subTopics (null 금지)
                "topic-" + id,
                "content",
                ImportanceLevel.DEFAULT,
                (int) (id % 100),
                null,
                null,
                null,
                false,
                false);
    }

    private static RoadMapView roadmap(long id, List<TopicView> topics, boolean isDraft) {
        return new RoadMapView(
                id,
                "rm-title",
                "rm-desc",
                null,
                null,
                null,
                false,
                isDraft, // isDeleted, isDraft
                777L,
                topics);
    }

    private static Travel makeTravelFromRoadmap(UserView user, RoadMapView rm) {
        var topicCmds =
                rm.topics().stream()
                        .map(
                                t ->
                                        new ProgressTopicCommand(
                                                t.id(),
                                                t.subTopics().stream()
                                                        .map(
                                                                st ->
                                                                        new ProgressSubTopicCommand(
                                                                                st.id()))
                                                        .toList()))
                        .toList();
        return Travel.create(new TravelCommand(user.id(), rm.id(), topicCmds));
    }

    @Test
    @DisplayName("create: 유효 입력이면 user/roadmap 조회 → 도메인 생성 → 저장 호출")
    void create_success() {
        // given
        Long userId = 1L, roadmapId = 100L;
        var user = new UserView(userId, null, AccountStatus.ACTIVE, null);

        var topics =
                IntStream.rangeClosed(1, 3)
                        .mapToObj(
                                i ->
                                        topic(
                                                i * 10L,
                                                IntStream.rangeClosed(1, 2)
                                                        .mapToObj(j -> sub(i * 10L + j))
                                                        .toList()))
                        .toList();
        var rm = roadmap(roadmapId, topics, true);

        var travel = makeTravelFromRoadmap(user, rm);

        when(userService.getByUser(userId)).thenReturn(user);
        when(roadMapQueryService.findById(roadmapId)).thenReturn(rm);
        when(travelCreationService.create(user, rm)).thenReturn(travel);

        when(travelRepository.save(any(Travel.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        service.create(userId, roadmapId);

        // then
        verify(userService).getByUser(userId);
        verify(roadMapQueryService).findById(roadmapId);
        verify(travelCreationService).create(user, rm);

        ArgumentCaptor<Travel> captor = ArgumentCaptor.forClass(Travel.class);
        verify(travelRepository).save(captor.capture());
        var saved = captor.getValue();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getRoadMapId()).isEqualTo(roadmapId);
        assertThat(saved.getTopics().size()).isEqualTo(3);
        long subCount = saved.getTopics().stream().flatMap(t -> t.getSubTopics().stream()).count();
        assertThat(subCount).isEqualTo(3L * 2L);
    }

    @Test
    @DisplayName("update: travel 조회 → user 조회 → 도메인 업데이트 → View 변환 반환")
    void update_success() {
        // given
        Long userId = 1L, roadmapId = 100L, travelId = 999L;
        var user = new UserView(userId, null, AccountStatus.ACTIVE, null);

        var topics =
                List.of(
                        topic(10L, List.of(sub(11L), sub(12L))),
                        topic(20L, List.of(sub(21L), sub(22L))));
        var rm = roadmap(roadmapId, topics, true);
        var travel = makeTravelFromRoadmap(user, rm);

        var req =
                new TravelUpdateRequest(
                        userId,
                        travelId,
                        List.of(new ProgressTopicUpdateRequest(20L, ProgressStatus.DONE)),
                        List.of(
                                new ProgressSubTopicUpdateRequest(
                                        10L, 12L, ProgressStatus.IN_PROGRESS)));

        when(travelRepository.findBatchById(travelId)).thenReturn(Optional.of(travel));
        when(userService.getByUser(userId)).thenReturn(user);

        when(travelUpdateService.update(user, travel, req))
                .thenAnswer(
                        inv -> {
                            travel.markTopic(20L, ProgressStatus.DONE);
                            travel.markSubTopic(10L, 12L, ProgressStatus.IN_PROGRESS);
                            return travel;
                        });

        // when
        TravelView view = service.update(req);

        // then
        verify(travelRepository).findBatchById(travelId);
        verify(userService).getByUser(userId);
        verify(travelUpdateService).update(user, travel, req);

        assertThat(view.userId()).isEqualTo(userId);
        assertThat(view.roadmapId()).isEqualTo(roadmapId);
        assertThat(view.progressTopicViews().size()).isEqualTo(2);

        var topic20 =
                view.progressTopicViews().stream()
                        .filter(v -> v.topicId().equals(20L))
                        .findFirst()
                        .orElseThrow();
        assertThat(topic20.status()).isEqualTo(ProgressStatus.DONE);

        var topic10 =
                view.progressTopicViews().stream()
                        .filter(v -> v.topicId().equals(10L))
                        .findFirst()
                        .orElseThrow();
        var sub12 =
                topic10.progressSubTopicViews().stream()
                        .filter(sv -> sv.subTopicId().equals(12L))
                        .findFirst()
                        .orElseThrow();
        assertThat(sub12.status()).isEqualTo(ProgressStatus.IN_PROGRESS);
    }
}
