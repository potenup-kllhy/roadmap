package com.kllhy.roadmap.roadmap.application.query.dto;

import java.sql.Timestamp;
import java.util.List;

public record RoadMapView(
        long id,
        String title,
        String description,
        Timestamp deletedAt,
        Timestamp createdAt,
        Timestamp modifiedAt,
        boolean isDeleted,
        boolean isDraft,
        long categoryId,
        List<TopicView> topics) {
    public RoadMapView {
        if (topics == null) {
            throw new IllegalArgumentException("로드맵은 1개 이상의 토픽을 가짐");
        }
        topics = List.copyOf(topics);
    }
}
