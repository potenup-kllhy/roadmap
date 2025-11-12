package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class RoadMapDeleted extends BaseDomainEvent {

    private final Long roadMapId;
    private final List<Long> topicIds = new ArrayList<>();
    private final List<Long> subTopicIds = new ArrayList<>();

    public RoadMapDeleted(Long roadMapId, List<Long> topicIds, List<Long> subTopicIds) {
        super();
        this.roadMapId = roadMapId;
        this.topicIds.addAll(topicIds);
        this.subTopicIds.addAll(subTopicIds);
    }

    public Long roadMapId() {
        return roadMapId;
    }

    public List<Long> topicIds() {
        return List.copyOf(topicIds);
    }

    public List<Long> subTopicIds() {
        return List.copyOf(subTopicIds);
    }
}
