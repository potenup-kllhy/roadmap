package com.kllhy.roadmap.roadmap.domain.event.listener;

import com.kllhy.roadmap.roadmap.domain.event.RoadMapEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import jakarta.persistence.PostPersist;

public class RoadMapEntityListener {

    @PostPersist
    public void onPostPersist(RoadMap roadMap) {
        RoadMapEventOccurred event = new RoadMapEventOccurred(
                roadMap.getId(),
                EventType.CREATED,
                roadMap.isDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE
        );

        SpringDomainEventPublisher.publish(event);
    }
}
