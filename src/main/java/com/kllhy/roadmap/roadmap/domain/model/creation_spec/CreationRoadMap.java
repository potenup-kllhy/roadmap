package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import java.util.List;

public record CreationRoadMap(
        String title,
        String description,
        boolean isDraft,
        Long categoryId,
        List<CreationTopic> creationTopics) {}
