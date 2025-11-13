package com.kllhy.roadmap.travel.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.domain.model.ProgressTopic;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TravelCreationServiceTest {
    private final TravelCreationService service = new TravelCreationService();

    private static Travel makeTravel(long userId, long roadmapId, Map<Long, List<Long>> structure) {
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
        return Travel.create(new TravelCommand(userId, roadmapId, topicCmds));
    }

    private static Set<Long> topicIdsOf(Travel t) {
        return t.getTopics().stream().map(ProgressTopic::getTopicId).collect(Collectors.toSet());
    }

    private static Set<Long> subTopicIdsOf(Travel t, long topicId) {
        return t.getTopicOrThrow(topicId).getSubTopics().stream()
                .map(st -> st.getSubTopicId())
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("createTopic: 각 Travel에 새 토픽이 추가된다(중복 없이)")
    void createTopic_adds_topic_to_each_travel() {
        // given: 두 개의 Travel, 둘 다 topic=10 없음
        var t1 = makeTravel(1L, 100L, Map.of(1L, List.of(11L, 12L)));
        var t2 = makeTravel(2L, 100L, Map.of(2L, List.of(21L)));

        // when
        service.createTopic(List.of(t1, t2), 10L);

        // then
        assertThat(topicIdsOf(t1)).containsExactlyInAnyOrder(1L, 10L);
        assertThat(topicIdsOf(t2)).containsExactlyInAnyOrder(2L, 10L);

        // 새로 추가된 topic(10)은 서브토픽 없이 생성됨
        assertThat(subTopicIdsOf(t1, 10L)).isEmpty();
        assertThat(subTopicIdsOf(t2, 10L)).isEmpty();
    }

    @Test
    @DisplayName("createTopic: 이미 존재하는 토픽을 추가하려 하면 DomainException")
    void createTopic_duplicate_throws() {
        // given: 이미 topic=10이 있는 Travel
        var t = makeTravel(1L, 100L, Map.of(10L, List.of(101L)));

        // when/then
        assertThatThrownBy(() -> service.createTopic(List.of(t), 10L))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("createSubTopic: 각 Travel의 해당 topic 아래에 subTopic이 추가된다")
    void createSubTopic_adds_subtopic_under_topic() {
        // given: 둘 다 topic=10 존재, subTopic은 아직 없음
        var t1 = makeTravel(1L, 100L, Map.of(10L, List.of()));
        var t2 = makeTravel(2L, 100L, Map.of(10L, List.of()));

        // when
        service.createSubTopic(List.of(t1, t2), 10L, 555L);

        // then
        assertThat(subTopicIdsOf(t1, 10L)).containsExactly(555L);
        assertThat(subTopicIdsOf(t2, 10L)).containsExactly(555L);
    }

    @Test
    @DisplayName("createSubTopic: 대상 topic이 없으면 DomainException")
    void createSubTopic_missing_topic_throws() {
        // given: topic=10 이 없음
        var t = makeTravel(1L, 100L, Map.of(20L, List.of(201L)));

        // when/then
        assertThatThrownBy(() -> service.createSubTopic(List.of(t), 10L, 555L))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("createSubTopic: 동일 subTopicId를 같은 topic에 두 번 추가하면 DomainException")
    void createSubTopic_duplicate_sub_throws() {
        // given
        var t = makeTravel(1L, 100L, Map.of(10L, List.of(555L)));

        // when/then
        assertThatThrownBy(() -> service.createSubTopic(List.of(t), 10L, 555L))
                .isInstanceOf(DomainException.class);
    }
}
