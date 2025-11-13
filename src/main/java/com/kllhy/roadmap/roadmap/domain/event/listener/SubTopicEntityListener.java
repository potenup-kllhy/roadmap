package com.kllhy.roadmap.roadmap.domain.event.listener;

import com.kllhy.roadmap.roadmap.domain.event.SubTopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.roadmap.domain.model.SubTopic;
import jakarta.persistence.PostPersist;

public class SubTopicEntityListener {

    @PostPersist
    public void onPostPersist(SubTopic subTopic) {
        SubTopicEventOccurred event =
                new SubTopicEventOccurred(
                        subTopic.getTopic().getRoadMap().getId(),
                        subTopic.getTopic().getId(),
                        subTopic.getId(),
                        EventType.CREATED,
                        subTopic.getIsDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE);

        SpringDomainEventPublisher.publish(event);
    }
}
