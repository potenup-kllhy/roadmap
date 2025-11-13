package com.kllhy.roadmap.roadmap.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record RoadMapCreateRequest(
        @Min(1) long userId,
        @NotBlank @Size(max = 255) String title,
        @Size(max = 255) String description,
        boolean isDraft,
        @NotNull @Positive Long categoryId,
        @NotNull @Size(min = 1) @Valid List<TopicCreateRequest> topics) {}
