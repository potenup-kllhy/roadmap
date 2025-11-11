package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.util.List;

public record CreationTopic(
        String title,
        String content,
        ImportanceLevel importanceLevel,
        Integer order,
        boolean isDraft,
        List<CreationResourceTopic> creationResourceTopics,
        List<CreationSubTopic> creationSubTopics) {}
