package com.kllhy.roadmap.roadmap.domain.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import java.util.ArrayList;
import java.util.List;

public class TopicDeleted extends BaseDomainEvent {

    private final Long topicId;
    private final List<Long> subTopicIds = new ArrayList<>();

    public TopicDeleted(Long topicId, List<Long> subTopicIds) {
        this.topicId = topicId;
        this.subTopicIds.addAll(subTopicIds);
    }

    public Long topicId() {
        return topicId;
    }

    public List<Long> subTopicIds() {
        return List.copyOf(subTopicIds);
    }
}
