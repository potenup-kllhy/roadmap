package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import java.util.Objects;

public record CreationResourceSubTopic(
        String name, int order, ResourceType resourceType, String link) {

    public CreationResourceSubTopic {
        Objects.requireNonNull(name, "CreationResourceSubTopic: name is not null");
        Objects.requireNonNull(name, "CreationResourceSubTopic: resourceType is not null");
        Objects.requireNonNull(name, "CreationResourceSubTopic: link is not null");
    }
}
