package com.kllhy.roadmap.roadmap.application.query.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.sql.Timestamp;
import java.util.List;

public record SubTopicView(
        long id,
        String title,
        String content,
        ImportanceLevel importanceLevel,
        Timestamp deletedAt,
        boolean isDraft,
        boolean isDeleted,
        List<ResourceSubTopicView> resources,
        Timestamp createdAt,
        Timestamp modifiedAt) {
    public SubTopicView {
        resources = List.copyOf(resources);
    }
}
