package com.kllhy.roadmap.travel.application.view;

import com.kllhy.roadmap.travel.domain.model.ProgressTopic;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import java.util.List;

public record ProgressTopicView(
        Long progressTopicId,
        Long topicId,
        ProgressStatus status,
        List<ProgressSubTopicView> progressSubTopicViews) {
    public static ProgressTopicView of(ProgressTopic progressTopic) {

        List<ProgressSubTopicView> subTopicViews =
                progressTopic.getSubTopics().stream().map(ProgressSubTopicView::of).toList();

        return new ProgressTopicView(
                progressTopic.getId(),
                progressTopic.getTopicId(),
                progressTopic.getStatus(),
                List.copyOf(subTopicViews));
    }
}
