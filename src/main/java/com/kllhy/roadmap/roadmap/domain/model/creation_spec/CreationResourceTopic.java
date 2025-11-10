package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;

public record CreationResourceTopic (
        String name,
        ResourceType resourceType,
        Integer order,
        String link
) {
}
