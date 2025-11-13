package com.kllhy.roadmap.travel.presentation.request;

import jakarta.validation.constraints.NotNull;

public record TravelCreateRequest(@NotNull Long userId, @NotNull Long roadmapId) {}
