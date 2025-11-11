package com.kllhy.roadmap.roadmap.application.query.dto;

import com.kllhy.roadmap.roadmap.persistence.model.enums.ImportanceLevel;
import java.sql.Timestamp;
import java.util.List;

public record TopicView(
        long id,
        List<ResourceTopicView> resources,
        List<SubTopicView> subTopics,
        String title,
        String content,
        ImportanceLevel importanceLevel,
        int order,
        Timestamp createdAt,
        Timestamp modifiedAt,
        Timestamp deletedAt,
        boolean isDraft,
        boolean isDeleted) {
    public TopicView {
        resources = List.copyOf(resources);
        subTopics = List.copyOf(subTopics);
    }
}
