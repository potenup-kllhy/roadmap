package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;

public class TopicEventOccurred extends BaseDomainEvent {

    private final Long roadMapId;
    private final Long topicId;
    private final EventType eventType;
    private final ActiveStatus activeStatus;

    public TopicEventOccurred(Long roadMapId, Long topicId, EventType eventType, ActiveStatus activeStatus) {
        this.roadMapId = roadMapId;
        this.topicId = topicId;
        this.eventType = eventType;
        this.activeStatus = activeStatus;
    }

    public Long roadMapId() {
        return roadMapId;
    }

    public Long topicId() {
        return topicId;
    }

    public EventType eventType() {
        return eventType;
    }

    public ActiveStatus activeStatus() {
        return activeStatus;
    }
}
