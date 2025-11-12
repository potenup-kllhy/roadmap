package com.kllhy.roadmap.travel.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.kllhy.roadmap.travel.domain.model.ProgressTopic;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TravelDomainTest {
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
}
