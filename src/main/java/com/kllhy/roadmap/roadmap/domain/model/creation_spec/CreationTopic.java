package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record CreationTopic(
        String title,
        String content,
        ImportanceLevel importanceLevel,
        int order,
        boolean isDraft,
        List<CreationResourceTopic> creationResourceTopics,
        List<CreationSubTopic> creationSubTopics) {

    public CreationTopic {
        Objects.requireNonNull(title, "CreationTopic: title is null");
        Objects.requireNonNull(importanceLevel, "CreationTopic: importanceLevel is null");
        if (creationResourceTopics == null) {
            creationResourceTopics = new ArrayList<>();
        }
        if (creationSubTopics == null) {
            creationSubTopics = new ArrayList<>();
        }
    }
}
