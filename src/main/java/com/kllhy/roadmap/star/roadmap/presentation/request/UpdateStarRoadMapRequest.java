package com.kllhy.roadmap.star.roadmap.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateStarRoadMapRequest(
        @NotNull Long userId,
        @NotNull @PositiveOrZero int value
) {
}
