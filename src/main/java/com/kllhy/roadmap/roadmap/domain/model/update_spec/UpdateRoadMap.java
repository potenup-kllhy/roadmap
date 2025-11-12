package com.kllhy.roadmap.roadmap.domain.model.update_spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UpdateRoadMap (
        // To Do: id?
        String title,
        String description,
        boolean isDraft,
        Long categoryId,
        List<UpdateTopic> topics) {

    public UpdateRoadMap {
        Objects.requireNonNull(title, "UpdateRoadMap: title is null");
        Objects.requireNonNull(description, "UpdateRoadMap: description is null");
        // CreationRoadMap 과는 달리 UpdateRoadMap 에서는 topics 가 null 일 수 있음
        if (topics == null) {
            topics = new ArrayList<>();
        }
        topics = List.copyOf(topics);
    }
}
