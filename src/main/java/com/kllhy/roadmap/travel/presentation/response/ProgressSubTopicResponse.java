package com.kllhy.roadmap.travel.presentation.response;

import com.kllhy.roadmap.travel.application.view.ProgressSubTopicView;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;

public record ProgressSubTopicResponse(
        Long progressSubTopicId, Long subTopicId, ProgressStatus status) {
    public static ProgressSubTopicResponse of(ProgressSubTopicView progressSubTopic) {
        return new ProgressSubTopicResponse(
                progressSubTopic.progressSubTopicId(),
                progressSubTopic.subTopicId(),
                progressSubTopic.status());
    }
}
