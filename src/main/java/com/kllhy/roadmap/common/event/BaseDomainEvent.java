package com.kllhy.roadmap.common.event;

import java.time.LocalDateTime;

public class BaseDomainEvent implements DomainEvent {
    private final LocalDateTime occurredOn;
    private final String eventName;

    protected BaseDomainEvent() {
        this(null, null);
    }

    protected BaseDomainEvent(String eventName, LocalDateTime occurredOn) {
        this.eventName = eventName != null ? eventName : getClass().getSimpleName();
        this.occurredOn = occurredOn != null ? occurredOn : LocalDateTime.now();
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
