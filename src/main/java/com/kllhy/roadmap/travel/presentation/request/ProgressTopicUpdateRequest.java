package com.kllhy.roadmap.travel.presentation.request;

import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;

public record ProgressTopicUpdateRequest(Long progressTopicId, ProgressStatus status) {}
