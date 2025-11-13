package com.kllhy.roadmap.travel.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.travel.domain.model.ProgressTopic;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TravelDomainTest {

    private static Travel travel(long userId, long rmId, Map<Long, List<Long>> structure) {
        var topicCmds =
                structure.entrySet().stream()
                        .map(
                                e ->
                                        new ProgressTopicCommand(
                                                e.getKey(),
                                                e.getValue() == null
                                                        ? List.of()
                                                        : e.getValue().stream()
                                                                .map(ProgressSubTopicCommand::new)
                                                                .toList()))
                        .toList();
        return Travel.create(new TravelCommand(userId, rmId, topicCmds));
    }

    @Test
    @DisplayName("create success Travel")
    void create_success() {
        Long userId = 1L;
        Long roadMapId = 100L;

        List<ProgressTopicCommand> topicCommands =
                IntStream.rangeClosed(1, 10)
                        .mapToObj(
                                i -> {
                                    Long topicId = (long) i * 10;
                                    List<ProgressSubTopicCommand> subTopicCommands =
                                            IntStream.rangeClosed(1, 5)
                                                    .mapToObj(
                                                            j ->
                                                                    new ProgressSubTopicCommand(
                                                                            topicId + j))
                                                    .toList();
                                    return new ProgressTopicCommand(topicId, subTopicCommands);
                                })
                        .toList();

        TravelCommand travelCommand = new TravelCommand(userId, roadMapId, topicCommands);

        Travel travel = Travel.create(travelCommand);

        // then
        assertThat(travel.getUserId()).isEqualTo(userId);
        assertThat(travel.getRoadMapId()).isEqualTo(roadMapId);
        assertThat(travel.getTopics().size()).isEqualTo(topicCommands.size());
        assertThat(travel.getTopics().stream().map(ProgressTopic::getSubTopics).toList().size())
                .isEqualTo(topicCommands.size());
    }

    @Test
    void update_success() {
        // given
        Long userId = 1L, roadmapId = 100L;
        var topicCmds =
                IntStream.rangeClosed(1, 3)
                        .mapToObj(
                                i -> {
                                    long topicId = i * 10L;
                                    var subCmds =
                                            IntStream.rangeClosed(1, 2)
                                                    .mapToObj(
                                                            j ->
                                                                    new ProgressSubTopicCommand(
                                                                            topicId + j))
                                                    .toList();
                                    return new ProgressTopicCommand(topicId, subCmds);
                                })
                        .toList();

        Travel travel = Travel.create(new TravelCommand(userId, roadmapId, topicCmds));

        // when
        travel.markTopic(20L, ProgressStatus.DONE);
        travel.markSubTopic(30L, 32L, ProgressStatus.IN_PROGRESS);

        // then
        var t20 =
                travel.getTopics().stream()
                        .filter(t -> t.getTopicId().equals(20L))
                        .findFirst()
                        .orElseThrow();
        assertThat(t20.getStatus()).isEqualTo(ProgressStatus.DONE);

        var t30 =
                travel.getTopics().stream()
                        .filter(t -> t.getTopicId().equals(30L))
                        .findFirst()
                        .orElseThrow();
        var st32 =
                t30.getSubTopics().stream()
                        .filter(st -> st.getSubTopicId().equals(32L))
                        .findFirst()
                        .orElseThrow();
        assertThat(st32.getStatus()).isEqualTo(ProgressStatus.IN_PROGRESS);

        assertThat(travel.getStatus()).isEqualTo(TravelProgressStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("create: topics null/empty면 예외")
    void create_invalid_topics() {
        // null
        assertThatThrownBy(() -> Travel.create(new TravelCommand(1L, 1L, null)))
                .isInstanceOf(DomainException.class);
        // empty
        assertThatThrownBy(() -> Travel.create(new TravelCommand(1L, 1L, List.of())))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("getTopics는 defensive copy를 반환한다(외부 변경 불가)")
    void topics_are_immutable_view() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        var view = tr.getTopics();
        assertThatThrownBy(() -> view.add(tr.getTopics().get(0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Travel이 INACTIVE면 변경 API(mark/activate 등) 사용 시 예외")
    void disallow_changes_when_archived() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        tr.activate(ActiveStatus.INACTIVE);

        assertThatThrownBy(() -> tr.markTopic(10L, ProgressStatus.DONE))
                .isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> tr.addTopics(List.of(new ProgressTopicCommand(20L, List.of()))))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("activate/activateTopic/activateSubTopic: 상태 토글이 정상 동작")
    void activate_flags_work() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        // root
        tr.activate(ActiveStatus.INACTIVE);
        assertThatThrownBy(() -> tr.markTopic(10L, ProgressStatus.DONE))
                .isInstanceOf(DomainException.class);

        // 다시 활성화해서 하위 테스트
        tr.activate(ActiveStatus.ACTIVE);
        tr.activateTopic(10L, ActiveStatus.INACTIVE); // topic 비활성
        var topic = tr.getTopicOrThrow(10L);
        assertThat(topic.isArchived()).isTrue();

        // subTopic 활성 토글
        tr.activateSubTopic(10L, 101L, ActiveStatus.INACTIVE);
        var sub = topic.getSubTopicOrThrow(101L);
        assertThat(sub.isArchived()).isTrue();
    }

    @Test
    @DisplayName("addTopics: 같은 topicId는 중복 불가")
    void prevent_topic_duplication() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        assertThatThrownBy(() -> tr.addTopics(List.of(new ProgressTopicCommand(10L, List.of()))))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("addSubTopics: 같은 subTopicId는 중복 불가")
    void prevent_subtopic_duplication() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        assertThatThrownBy(() -> tr.addSubTopics(10L, List.of(new ProgressSubTopicCommand(101L))))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("markTopic: 정상 변경 시 토픽 상태가 갱신된다")
    void mark_topic_ok() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        tr.markTopic(10L, ProgressStatus.DONE);
        assertThat(tr.getTopicOrThrow(10L).getStatus()).isEqualTo(ProgressStatus.DONE);
    }

    @Test
    @DisplayName("markSubTopic: 정상 변경 시 서브토픽 상태가 갱신된다")
    void mark_subtopic_ok() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L, 102L)));
        tr.markSubTopic(10L, 102L, ProgressStatus.IN_PROGRESS);
        var sub = tr.getTopicOrThrow(10L).getSubTopicOrThrow(102L);
        assertThat(sub.getStatus()).isEqualTo(ProgressStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("markTopic: 존재하지 않는 topicId면 예외")
    void mark_topic_not_found() {
        var tr = travel(1L, 100L, Map.of(10L, List.of()));
        assertThatThrownBy(() -> tr.markTopic(999L, ProgressStatus.DONE))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("markSubTopic: topicId는 존재하나 subTopicId가 없으면 예외")
    void mark_subtopic_not_found() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        assertThatThrownBy(() -> tr.markSubTopic(10L, 999L, ProgressStatus.DONE))
                .isInstanceOf(DomainException.class);
    }

    // ===== 상태 집계 =====
    @Test
    @DisplayName("getStatus: TODO/IN_PROGRESS가 하나라도 있으면 IN_PROGRESS")
    void status_in_progress_when_any_not_done() {
        var tr =
                travel(
                        1L,
                        100L,
                        Map.of(
                                10L, List.of(101L, 102L),
                                20L, List.of(201L)));
        tr.markTopic(20L, ProgressStatus.DONE);
        tr.markSubTopic(10L, 101L, ProgressStatus.DONE);
        // 아직 10L의 topic 자체와 10L-102L, 20L-201L는 TODO → IN_PROGRESS
        assertThat(tr.getStatus()).isEqualTo(TravelProgressStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("getStatus: 모든 표시 가능한(archived=false) 토픽/서브토픽이 DONE이면 DONE")
    void status_done_when_all_done() {
        var tr =
                travel(
                        1L,
                        100L,
                        Map.of(
                                10L, List.of(101L, 102L),
                                20L, List.of(201L)));
        tr.markTopic(10L, ProgressStatus.DONE);
        tr.markSubTopic(10L, 101L, ProgressStatus.DONE);
        tr.markSubTopic(10L, 102L, ProgressStatus.DONE);
        tr.markTopic(20L, ProgressStatus.DONE);
        tr.markSubTopic(20L, 201L, ProgressStatus.DONE);

        assertThat(tr.getStatus()).isEqualTo(TravelProgressStatus.DONE);
    }

    @Test
    @DisplayName("getStatus: 미완료가 있어도 전부 archived면 DONE으로 간주되지 않음(집계 규칙 검증)")
    void status_ignores_archived_nodes() {
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L, 102L)));
        tr.markSubTopic(10L, 101L, ProgressStatus.DONE);
        // 102는 TODO지만 archived 처리 → 집계에서 제외
        tr.activateSubTopic(10L, 102L, ActiveStatus.INACTIVE);

        // topic 10 자체는 TODO이므로 전체 상태는 IN_PROGRESS
        assertThat(tr.getStatus()).isEqualTo(TravelProgressStatus.IN_PROGRESS);

        // topic도 DONE으로 만들면 전체 DONE
        tr.markTopic(10L, ProgressStatus.DONE);
        assertThat(tr.getStatus()).isEqualTo(TravelProgressStatus.DONE);
    }

    // ===== 명령 유효화(Travel.addTopics / addSubTopics) =====
    @Test
    @DisplayName("addTopics: null/empty 명령은 예외")
    void add_topics_null_empty() {
        var tr = travel(1L, 100L, Map.of(10L, List.of()));
        assertThatThrownBy(() -> tr.addTopics(null)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> tr.addTopics(List.of())).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("addSubTopics: 대상 topic이 없으면 예외")
    void add_subtopics_topic_not_found() {
        var tr = travel(1L, 100L, Map.of(10L, List.of()));
        assertThatThrownBy(() -> tr.addSubTopics(999L, List.of(new ProgressSubTopicCommand(1L))))
                .isInstanceOf(DomainException.class);
    }

    // ===== 방어적 프로그래밍: 대량 생성 시 경계값 =====
    @Test
    @DisplayName("대량 subTopic 추가 시에도 중복/아카이브 규칙이 지켜진다")
    void bulk_subtopics() {
        var tr = travel(1L, 100L, Map.of(10L, List.of()));
        var many =
                IntStream.rangeClosed(1, 500)
                        .mapToObj(i -> new ProgressSubTopicCommand((long) i))
                        .toList();

        tr.addSubTopics(10L, many);
        assertThat(tr.getTopicOrThrow(10L).getSubTopics().size()).isEqualTo(500);

        // 중복 추가 시 예외
        assertThatThrownBy(() -> tr.addSubTopics(10L, List.of(new ProgressSubTopicCommand(1L))))
                .isInstanceOf(DomainException.class);
    }
}
