package com.kllhy.roadmap.roadmap.domain.event.listener;

import com.kllhy.roadmap.roadmap.domain.event.TopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.roadmap.domain.model.Topic;
import jakarta.persistence.PostPersist;

public class TopicEntityListener {

    @PostPersist
    public void onPostPersist(Topic topic) {
        TopicEventOccurred event =
                new TopicEventOccurred(
                        topic.getRoadMap().getId(),
                        topic.getId(),
                        EventType.CREATED,
                        topic.isDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE);

        SpringDomainEventPublisher.publish(event);
    }
}
