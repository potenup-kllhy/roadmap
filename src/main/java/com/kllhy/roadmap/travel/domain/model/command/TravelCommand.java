package com.kllhy.roadmap.travel.domain.model.command;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import java.util.List;
import java.util.Objects;

public record TravelCommand(Long userId, Long roadmapId, List<ProgressTopicCommand> topics) {
    public TravelCommand {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(roadmapId, "roadmapId must not be null");

        if (topics == null || topics.isEmpty()) {
            throw new DomainException(TravelErrorCode.TRAVEL_TOPICS_INVALID);
        }

        topics = topics.stream().filter(Objects::nonNull).toList();
    }
}
