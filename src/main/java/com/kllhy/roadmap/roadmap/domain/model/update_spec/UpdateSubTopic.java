package com.kllhy.roadmap.roadmap.domain.model.update_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UpdateSubTopic (
        Long id,
        String title,
        String content,
        ImportanceLevel importanceLevel,
        boolean isDraft,
        List<UpdateResourceSubTopic> updateResourceSubTopics) {

    public UpdateSubTopic {
        Objects.requireNonNull(title, "UpdateSubTopic: title is null");
        Objects.requireNonNull(importanceLevel, "UpdateSubTopic: importanceLevel is null");
        if (updateResourceSubTopics == null) {
            updateResourceSubTopics = new ArrayList<>();
        }
        updateResourceSubTopics = List.copyOf(updateResourceSubTopics);
    }
}
