package com.kllhy.roadmap.travel.domain.model.read;

import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import java.util.List;

public record TravelSnapshot(
        Long travelId,
        Long userId,
        Long roadMapId,
        TravelProgressStatus status,
        List<ProgressTopicSnapshot> topics) {

    public record ProgressTopicSnapshot(
            Long progressTopicId,
            Long topicId,
            ProgressStatus status,
            List<ProgressSubTopicSnapshot> subTopics) {}

    public record ProgressSubTopicSnapshot(
            Long progressSubTopicId, Long subTopicId, ProgressStatus status) {}
}
