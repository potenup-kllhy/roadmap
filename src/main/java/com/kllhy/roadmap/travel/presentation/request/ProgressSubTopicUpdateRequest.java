package com.kllhy.roadmap.travel.presentation.request;

import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;

public record ProgressSubTopicUpdateRequest(
        Long progressTopicId, Long progressSubTopicId, ProgressStatus status) {}
