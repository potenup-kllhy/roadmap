package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.Topic;

import java.util.List;

public record CreationRoadMap (
        String title,
        String description,
        boolean isDraft,
        Long categoryId,
        List<Topic> topics
) {
}
