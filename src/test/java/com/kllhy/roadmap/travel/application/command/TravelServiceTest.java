package com.kllhy.roadmap.travel.application.command;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.application.query.dto.SubTopicView;
import com.kllhy.roadmap.roadmap.application.query.dto.TopicView;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import com.kllhy.roadmap.travel.application.service.command.TravelServiceAdapter;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.model.ProgressSubTopic;
import com.kllhy.roadmap.travel.domain.model.ProgressTopic;
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
import java.util.Arrays;
import java.util.Collection;
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

    private static Travel makeTravel(
            long userId, long roadmapId, List<ProgressTopicCommand> topics) {
        return Travel.create(new TravelCommand(userId, roadmapId, topics));
    }

    private static ProgressTopicCommand topicOnly(long topicId) {
        // subtopic 없이 topic만
        return new ProgressTopicCommand(topicId, List.of());
    }

    private static ProgressTopicCommand topicWithSubs(long topicId, long... subIds) {
        var subs = Arrays.stream(subIds).mapToObj(ProgressSubTopicCommand::new).toList();
        return new ProgressTopicCommand(topicId, subs);
    }

    private static ProgressTopic findTopic(Travel t, long topicId) {
        return t.getTopics().stream()
                .filter(tp -> tp.getTopicId().equals(topicId))
                .findFirst()
                .orElseThrow();
    }

    private static ProgressSubTopic findSub(Travel t, long topicId, long subId) {
        var topic = findTopic(t, topicId);
        return topic.getSubTopics().stream()
                .filter(st -> st.getSubTopicId().equals(subId))
                .findFirst()
                .orElseThrow();
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

    @Test
    @DisplayName("syncArchivedOnRoadmap: 같은 로드맵의 모든 Travel이 활성/비활성 반영")
    void syncArchivedOnRoadmap() {
        long roadmapId = 100L;

        var t1 = makeTravel(1L, roadmapId, List.of(topicOnly(10L)));
        var t2 = makeTravel(2L, roadmapId, List.of(topicOnly(20L)));
        when(travelRepository.findAllByRoadMapId(roadmapId)).thenReturn(List.of(t1, t2));

        service.syncArchivedOnRoadmap(roadmapId, ActiveStatus.INACTIVE);

        assertThat(t1.isArchived()).isTrue();
        assertThat(t2.isArchived()).isTrue();

        // 다시 활성화
        service.syncArchivedOnRoadmap(roadmapId, ActiveStatus.ACTIVE);
        assertThat(t1.isArchived()).isFalse();
        assertThat(t2.isArchived()).isFalse();

        verify(travelRepository, times(2)).findAllByRoadMapId(roadmapId);
    }

    @Test
    @DisplayName("syncArchivedOnTopic: 해당 topicId 가진 모든 Travel의 해당 토픽만 활성/비활성 반영")
    void syncArchivedOnTopic() {
        long topicId = 10L;

        var t = makeTravel(1L, 200L, List.of(topicOnly(10L), topicOnly(20L)));
        when(travelRepository.findAllByTopicId(topicId)).thenReturn(List.of(t));

        // 비활성화
        service.syncArchivedOnTopic(topicId, ActiveStatus.INACTIVE);
        assertThat(findTopic(t, 10L).isArchived()).isTrue();
        assertThat(findTopic(t, 20L).isArchived()).isFalse(); // 다른 토픽 영향 없음

        // 활성화
        service.syncArchivedOnTopic(topicId, ActiveStatus.ACTIVE);
        assertThat(findTopic(t, 10L).isArchived()).isFalse();

        verify(travelRepository, times(2)).findAllByTopicId(topicId);
    }

    @Test
    @DisplayName("syncCreateOnTopic: 해당 topicId가 있는 모든 Travel에 topic 생성 위임")
    void syncCreateOnTopic() {
        long topicId = 99L;

        var t1 = makeTravel(1L, 300L, List.of(topicOnly(10L)));
        var t2 = makeTravel(2L, 300L, List.of(topicOnly(20L)));
        when(travelRepository.findAllByTopicId(topicId)).thenReturn(List.of(t1, t2));

        service.syncCreateOnTopic(topicId);

        verify(travelCreationService).createTopic(List.of(t1, t2), topicId);
    }

    @Test
    @DisplayName(
            "syncArchivedOnSubTopic: subTopicId가 속한 모든 Travel에서 해당 (topicId, subTopicId)만 활성/비활성")
    void syncArchivedOnSubTopic() {
        long topicId = 10L;
        long subId = 101L;

        var t1 =
                makeTravel(
                        1L,
                        400L,
                        List.of(topicWithSubs(10L, 100L, 101L, 102L), topicWithSubs(20L, 201L)));
        var t2 =
                makeTravel(
                        2L,
                        400L,
                        List.of(
                                topicWithSubs(10L, 101L), // 같은 subId 포함
                                topicWithSubs(30L, 301L)));

        // findTravelIdsBySubTopicIds → findByIdIn 체인 스텁
        when(travelRepository.findTravelIdsBySubTopicIds(List.of(subId)))
                .thenReturn(List.of(1L, 2L)); // 어떤 값이든 상관없음(테스트용)
        when(travelRepository.findByIdIn(any(Collection.class))).thenReturn(List.of(t1, t2));

        // 비활성화
        service.syncArchivedOnSubTopic(topicId, subId, ActiveStatus.INACTIVE);

        assertThat(findSub(t1, 10L, 101L).isArchived()).isTrue();
        assertThat(findSub(t1, 10L, 100L).isArchived()).isFalse(); // 다른 sub는 영향 X
        assertThat(findSub(t2, 10L, 101L).isArchived()).isTrue();

        // 활성화
        service.syncArchivedOnSubTopic(topicId, subId, ActiveStatus.ACTIVE);
        assertThat(findSub(t1, 10L, 101L).isArchived()).isFalse();
        assertThat(findSub(t2, 10L, 101L).isArchived()).isFalse();

        verify(travelRepository, times(2)).findTravelIdsBySubTopicIds(List.of(subId));
        verify(travelRepository, times(2)).findByIdIn(any(Collection.class));
    }

    @Test
    @DisplayName("syncCreateOnSubTopic: subTopicId가 속한 모든 Travel에 subTopic 생성 위임")
    void syncCreateOnSubTopic() {
        long topicId = 10L;
        long subId = 555L;

        var t1 = makeTravel(1L, 500L, List.of(topicOnly(10L)));
        var t2 = makeTravel(2L, 500L, List.of(topicOnly(10L), topicOnly(20L)));

        when(travelRepository.findTravelIdsBySubTopicIds(List.of(subId)))
                .thenReturn(List.of(11L, 22L));
        when(travelRepository.findByIdIn(List.of(11L, 22L))).thenReturn(List.of(t1, t2));

        service.syncCreateOnSubTopic(topicId, subId);

        verify(travelCreationService).createSubTopic(List.of(t1, t2), topicId, subId);
    }
}
