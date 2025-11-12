package com.kllhy.roadmap.roadmap.domain.model.update_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;

import java.util.Objects;

public record UpdateResourceTopic (
        Long id, String name, Integer order, ResourceType resourceType, String link) {

    public UpdateResourceTopic {
        Objects.requireNonNull(name, "UpdateResourceSubTopic: name is null");
        Objects.requireNonNull(order, "UpdateResourceSubTopic: order is null");
        Objects.requireNonNull(resourceType, "UpdateResourceSubTopic: resourceType is null");
        Objects.requireNonNull(link, "UpdateResourceSubTopic: link is null");
    }
}
