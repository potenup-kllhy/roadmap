package com.kllhy.roadmap.travel.presentation.request;

import java.util.List;

public record TravelUpdateRequest(
        Long userId,
        Long travelId,
        List<ProgressTopicUpdateRequest> progressTopicUpdates,
        List<ProgressSubTopicUpdateRequest> progressSubTopicUpdateRequests) {}
