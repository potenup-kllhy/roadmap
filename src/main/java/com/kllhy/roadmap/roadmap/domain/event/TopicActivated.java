package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;

public class TopicActivated extends BaseDomainEvent {

    private final Long topicId;

    public TopicActivated(Long topicId) {
        this.topicId = topicId;
    }

    public Long topicId() {
        return topicId;
    }
}
