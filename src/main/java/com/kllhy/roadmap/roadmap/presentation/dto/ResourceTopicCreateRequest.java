package com.kllhy.roadmap.roadmap.presentation.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import jakarta.validation.constraints.*;

public record ResourceTopicCreateRequest(
        @Size(min = 1) String name,
        @NotNull ResourceType resourceType,
        @NotNull @Positive Integer order,
        @NotBlank @Size(max = 255) @Pattern(regexp = "^https?://.+$") String link) {}
