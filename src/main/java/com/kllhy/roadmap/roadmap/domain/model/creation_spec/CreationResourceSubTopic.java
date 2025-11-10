package com.kllhy.roadmap.roadmap.domain.model.creation_spec;

import com.kllhy.roadmap.roadmap.domain.model.SubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;

public record CreationResourceSubTopic (
        String name,
        Integer order,
        ResourceType resourceType,
        String link,
        SubTopic subTopic
) {
}
