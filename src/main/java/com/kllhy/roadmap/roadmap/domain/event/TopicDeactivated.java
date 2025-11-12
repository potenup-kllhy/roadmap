package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;

public class TopicDeactivated extends BaseDomainEvent {

    private final Long topicId;

    public TopicDeactivated(Long topicId) {
        this.topicId = topicId;
    }

    public Long topicId() {
        return topicId;
    }
}
