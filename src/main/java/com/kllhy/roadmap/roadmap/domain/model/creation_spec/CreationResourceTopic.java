package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import java.util.Objects;

public record CreationResourceTopic(
        String name, ResourceType resourceType, int order, String link) {

    public CreationResourceTopic {
        Objects.requireNonNull(name, "CreationResourceTopic: name is null");
        Objects.requireNonNull(resourceType, "CreationResourceTopic: resourceType is null");
        Objects.requireNonNull(link, "CreationResourceTopic: link is null");
    }
}
