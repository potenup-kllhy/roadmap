package com.kllhy.roadmap.travel.domain.model.command;

import java.util.Objects;

public record ProgressSubTopicCommand(Long subTopicId) {
    public ProgressSubTopicCommand {
        Objects.requireNonNull(subTopicId, "subTopicId must not be null");
    }
}
