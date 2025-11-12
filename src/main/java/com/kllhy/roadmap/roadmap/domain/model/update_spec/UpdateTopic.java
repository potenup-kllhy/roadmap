package com.kllhy.roadmap.roadmap.domain.model.update_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UpdateTopic (
        Long id,
        String title,
        String content,
        ImportanceLevel importanceLevel,
        Integer order,
        boolean isDraft,
        List<UpdateResourceTopic> resourceTopics,
        List<UpdateSubTopic> subTopics) {

    public UpdateTopic {
        Objects.requireNonNull(title, "UpdateTopic: title is null");
        Objects.requireNonNull(importanceLevel, "UpdateTopic: importanceLevel is null");
        Objects.requireNonNull(order, "UpdateTopic: order is null");
        if (resourceTopics == null) {
            resourceTopics = new ArrayList<>();
        }
        if (subTopics == null) {
            subTopics = new ArrayList<>();
        }
        resourceTopics = List.copyOf(resourceTopics);
        subTopics = List.copyOf(subTopics);
    }
}
