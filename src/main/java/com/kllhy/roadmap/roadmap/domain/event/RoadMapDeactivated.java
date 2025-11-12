package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;

public class RoadMapDeactivated extends BaseDomainEvent {

    private final Long roadMapId;

    public RoadMapDeactivated(Long roadMapId) {
        this.roadMapId = roadMapId;
    }

    public Long roadMapId() {
        return roadMapId;
    }
}
