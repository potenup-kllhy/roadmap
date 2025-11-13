package com.kllhy.roadmap.travel.application.view;

import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import java.util.List;

public record TravelView(
        Long userId,
        Long roadmapId,
        TravelProgressStatus status,
        List<ProgressTopicView> progressTopicViews) {
    public static TravelView of(Travel travel) {

        List<ProgressTopicView> progressTopicViews =
                travel.getTopics().stream()
                        .filter(it -> !it.isArchived())
                        .map(ProgressTopicView::of)
                        .toList();

        return new TravelView(
                travel.getUserId(),
                travel.getRoadMapId(),
                travel.getStatus(),
                List.copyOf(progressTopicViews));
    }
}
