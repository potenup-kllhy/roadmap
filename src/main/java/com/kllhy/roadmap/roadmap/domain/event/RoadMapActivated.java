package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;

public class RoadMapActivated extends BaseDomainEvent {

    private final Long roadMapId;

    public RoadMapActivated(Long roadMapId) {
        this.roadMapId = roadMapId;
    }

    public Long roadMapId() {
        return roadMapId;
    }
}
