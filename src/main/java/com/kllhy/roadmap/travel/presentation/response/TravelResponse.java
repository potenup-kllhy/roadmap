package com.kllhy.roadmap.travel.presentation.response;

import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import java.util.List;

public record TravelResponse(
        Long userId,
        Long roadmapId,
        TravelProgressStatus status,
        List<ProgressTopicResponse> progressTopics) {
    public static TravelResponse of(TravelView travel) {

        List<ProgressTopicResponse> progressTopics =
                travel.progressTopicViews().stream().map(ProgressTopicResponse::of).toList();

        return new TravelResponse(
                travel.userId(), travel.roadmapId(), travel.status(), List.copyOf(progressTopics));
    }
}
