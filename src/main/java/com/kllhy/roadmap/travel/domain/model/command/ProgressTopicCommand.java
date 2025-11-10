package com.kllhy.roadmap.travel.domain.model.command;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import java.util.List;
import java.util.Objects;

public record ProgressTopicCommand(Long topicId, List<ProgressSubTopicCommand> subTopics) {
    public ProgressTopicCommand {
        if (topicId == null) throw new DomainException(TravelErrorCode.TRAVEL_TOPICS_INVALID);

        if (subTopics == null) {
            subTopics = List.of();
        } else {
            subTopics = subTopics.stream().filter(Objects::nonNull).toList();
        }
        subTopics = List.copyOf(subTopics);
    }
}
