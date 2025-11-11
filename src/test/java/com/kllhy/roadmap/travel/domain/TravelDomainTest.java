package com.kllhy.roadmap.travel.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
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
        assertThat(travel.getTopics().stream().map(it -> it.getSubTopics()).toList().size())
                .isEqualTo(topicCommands.size());
    }
}
