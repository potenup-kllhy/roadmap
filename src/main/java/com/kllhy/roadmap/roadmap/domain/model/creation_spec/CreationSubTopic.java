package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.ResourceSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;

import java.util.List;

public record CreationSubTopic (
        String title,
        String content,
        ImportanceLevel importanceLevel,
        boolean isDraft,
        List<ResourceSubTopic> resources
) {
}
