package com.kllhy.roadmap.roadmap.domain.event;

public class SubTopicDeactivated {

    private final Long subTopicId;

    public SubTopicDeactivated(Long subTopicId) {
        this.subTopicId = subTopicId;
    }

    public Long subTopicId() {
        return subTopicId;
    }
}
