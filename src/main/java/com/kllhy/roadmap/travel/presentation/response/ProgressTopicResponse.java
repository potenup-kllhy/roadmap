package com.kllhy.roadmap.travel.presentation.response;

import com.kllhy.roadmap.travel.application.view.ProgressTopicView;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import java.util.List;

public record ProgressTopicResponse(
        Long progressTopicId,
        Long topicId,
        ProgressStatus status,
        List<ProgressSubTopicResponse> progressSubTopics) {
    public static ProgressTopicResponse of(ProgressTopicView progressTopic) {

        List<ProgressSubTopicResponse> subTopics =
                progressTopic.progressSubTopics().stream()
                        .map(ProgressSubTopicResponse::of)
                        .toList();

        return new ProgressTopicResponse(
                progressTopic.progressTopicId(),
                progressTopic.topicId(),
                progressTopic.status(),
                List.copyOf(subTopics));
    }
}
