package com.kllhy.roadmap.travel.presentation.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TravelUpdateRequest(
        @NotNull Long userId,
        @NotNull Long travelId,
        List<ProgressTopicUpdateRequest> progressTopicUpdates,
        List<ProgressSubTopicUpdateRequest> progressSubTopicUpdateRequests) {}
