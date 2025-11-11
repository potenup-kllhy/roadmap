package com.kllhy.roadmap.travel.domain.model.command;

import java.util.List;
import java.util.Objects;

public record ProgressTopicCommand(Long topicId, List<ProgressSubTopicCommand> subTopics) {
    public ProgressTopicCommand {
        Objects.requireNonNull(topicId, "subTopicId must not be null");

        if (subTopics == null) {
            subTopics = List.of();
        } else {
            subTopics = subTopics.stream().filter(Objects::nonNull).toList();
        }
        subTopics = List.copyOf(subTopics);
    }
}
