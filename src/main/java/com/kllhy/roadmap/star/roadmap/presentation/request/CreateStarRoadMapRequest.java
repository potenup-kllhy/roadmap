package com.kllhy.roadmap.star.roadmap.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateStarRoadMapRequest(
        @NotNull Long userId,
        @NotNull Long roadmapId,
        @NotNull @PositiveOrZero int value
) {
}
