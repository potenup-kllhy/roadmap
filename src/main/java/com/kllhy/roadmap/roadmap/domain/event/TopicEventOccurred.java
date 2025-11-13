package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import java.util.UUID;

public class TopicEventOccurred extends BaseDomainEvent {

    private final UUID roadMapUUID;
    private final UUID topicUUID;
    private final EventType eventType;
    private final ActiveStatus activeStatus;

    public TopicEventOccurred(
            UUID roadMapUUID, UUID topicUUID, EventType eventType, ActiveStatus activeStatus) {
        this.roadMapUUID = roadMapUUID;
        this.topicUUID = topicUUID;
        this.eventType = eventType;
        this.activeStatus = activeStatus;
    }

    public UUID roadMapUUID() {
        return roadMapUUID;
    }

    public UUID topicUUID() {
        return topicUUID;
    }

    public EventType eventType() {
        return eventType;
    }

    public ActiveStatus activeStatus() {
        return activeStatus;
    }
}
