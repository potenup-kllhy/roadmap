package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.util.List;
import java.util.Objects;

public record CreationTopic(
        String title,
        String content,
        ImportanceLevel importanceLevel,
        Integer order,
        boolean isDraft,
        List<CreationResourceTopic> creationResourceTopics,
        List<CreationSubTopic> creationSubTopics) {

    public CreationTopic {
        Objects.requireNonNull(title, "CreationTopic: title is null");
        Objects.requireNonNull(importanceLevel, "CreationTopic: importanceLevel is null");
        Objects.requireNonNull(order, "CreationTopic: order is null");
    }
}
