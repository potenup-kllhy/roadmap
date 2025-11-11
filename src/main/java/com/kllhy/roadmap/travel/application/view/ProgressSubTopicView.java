package com.kllhy.roadmap.travel.application.view;

import com.kllhy.roadmap.travel.domain.model.ProgressSubTopic;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;

public record ProgressSubTopicView(
        Long progressSubTopicId, Long subTopicId, ProgressStatus status) {
    public static ProgressSubTopicView of(ProgressSubTopic progressSubTopic) {
        return new ProgressSubTopicView(
                progressSubTopic.getId(),
                progressSubTopic.getSubTopicId(),
                progressSubTopic.getStatus());
    }
}
