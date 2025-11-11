package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import java.util.List;
import java.util.Objects;

public record CreationRoadMap(
        String title, String description, boolean isDraft, Long categoryId, List<CreationTopic> creationTopics) {

    public CreationRoadMap {
        Objects.requireNonNull(title, "CreationRoadMap: title is null");
        Objects.requireNonNull(categoryId, "CreationRoadMap: categoryId is null");
    }
}
