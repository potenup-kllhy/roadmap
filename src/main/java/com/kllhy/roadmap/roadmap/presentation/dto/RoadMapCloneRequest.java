package com.kllhy.roadmap.roadmap.presentation.dto;

import jakarta.validation.constraints.Min;

public record RoadMapCloneRequest(@Min(1) long userId, @Min(1) long categoryId) {}
