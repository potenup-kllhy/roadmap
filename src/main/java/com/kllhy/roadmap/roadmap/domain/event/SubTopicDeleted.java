package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;

public class SubTopicDeleted extends BaseDomainEvent {

    private final Long subTopicId;

    public SubTopicDeleted(Long subTopicId) {
        this.subTopicId = subTopicId;
    }

    public Long subTopicId() {
        return subTopicId;
    }
}
