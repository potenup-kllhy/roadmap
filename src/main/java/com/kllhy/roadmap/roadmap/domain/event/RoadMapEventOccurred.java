package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;

public class RoadMapEventOccurred extends BaseDomainEvent {

    private final Long roadMapId;
    private final EventType eventType;
    private final ActiveStatus activeStatus;

    public RoadMapEventOccurred(Long roadMapId, EventType eventType, ActiveStatus activeStatus) {
        this.roadMapId = roadMapId;
        this.eventType = eventType;
        this.activeStatus = activeStatus;
    }

    public Long roadMapId() {
        return roadMapId;
    }

    public EventType eventType() {
        return eventType;
    }

    public ActiveStatus activeStatus() {
        return activeStatus;
    }
}
