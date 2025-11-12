package com.kllhy.roadmap.roadmap.domain.event;

public class SubTopicActivated {

    private final Long subTopicId;

    public SubTopicActivated(Long subTopicId) {
        this.subTopicId = subTopicId;
    }

    public Long subTopicId() {
        return subTopicId;
    }
}
