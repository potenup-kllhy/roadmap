package com.kllhy.roadmap.common.event;

import java.time.LocalDateTime;

public class BaseDomainEvent implements DomainEvent {
    private final LocalDateTime occurredOn;
    private final String eventName;

    protected BaseDomainEvent(String eventName) {
        this.occurredOn = LocalDateTime.now();
        this.eventName = eventName;
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    @Override
    public String getEventName() {
        return eventName;
    }
}
