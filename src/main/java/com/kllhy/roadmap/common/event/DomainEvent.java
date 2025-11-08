package com.kllhy.roadmap.common.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime getOccurredOn();

    String getEventName();
}
