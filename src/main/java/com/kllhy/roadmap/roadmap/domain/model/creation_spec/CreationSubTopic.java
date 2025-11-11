package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.util.List;
import java.util.Objects;

public record CreationSubTopic(
        String title,
        String content,
        ImportanceLevel importanceLevel,
        boolean isDraft,
        List<CreationResourceSubTopic> creationResourceSubTopics) {

    public CreationSubTopic {
        Objects.requireNonNull(title, "CreationSubTopic: title is null");
        Objects.requireNonNull(importanceLevel, "CreationSubTopic: importanceLevel is null");
    }
}