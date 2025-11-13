package com.kllhy.roadmap.star.roadmap.presentation.request;

import jakarta.validation.constraints.NotNull;

public record DeleteStarRoadMapRequest(@NotNull Long userId, @NotNull Long roadmapId) {
}
