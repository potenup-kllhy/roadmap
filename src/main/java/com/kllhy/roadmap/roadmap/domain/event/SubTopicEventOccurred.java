package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;

import java.util.UUID;

public class SubTopicEventOccurred extends BaseDomainEvent {

    private final UUID roadMapUUID;
    private final UUID topicUUID;
    private final UUID subTopicUUID;
    private final EventType eventType;
    private final ActiveStatus activeStatus;

    public SubTopicEventOccurred(
            UUID roadMapUUID,
            UUID topicUUID,
            UUID subTopicUUID,
            EventType eventType,
            ActiveStatus activeStatus) {
        this.roadMapUUID = roadMapUUID;
        this.topicUUID = topicUUID;
        this.subTopicUUID = subTopicUUID;
        this.eventType = eventType;
        this.activeStatus = activeStatus;
    }

    public UUID roadMapUUID() {
        return roadMapUUID;
    }

    public UUID topicUUID() {
        return topicUUID;
    }

    public UUID subTopicUUID() {
        return subTopicUUID;
    }

    public EventType eventType() {
        return eventType;
    }

    public ActiveStatus activeStatus() {
        return activeStatus;
    }
}
