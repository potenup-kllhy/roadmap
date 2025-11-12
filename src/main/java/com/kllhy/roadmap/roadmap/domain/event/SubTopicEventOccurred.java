package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;

public class SubTopicEventOccurred extends BaseDomainEvent {

    private final Long roadMapId;
    private final Long topicId;
    private final Long subTopicId;
    private final EventType eventType;
    private final ActiveStatus activeStatus;

    public SubTopicEventOccurred(Long roadMapId, Long topicId, Long subTopicId, EventType eventType, ActiveStatus activeStatus) {
        this.roadMapId = roadMapId;
        this.topicId = topicId;
        this.subTopicId = subTopicId;
        this.eventType = eventType;
        this.activeStatus = activeStatus;
    }

    public Long roadMapId() {
        return roadMapId;
    }

    public Long topicId() {
        return topicId;
    }

    public Long subTopicId() {
        return subTopicId;
    }

    public EventType eventType() {
        return eventType;
    }

    public ActiveStatus activeStatus() {
        return activeStatus;
    }
}
