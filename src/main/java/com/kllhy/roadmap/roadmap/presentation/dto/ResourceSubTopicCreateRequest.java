package com.kllhy.roadmap.roadmap.presentation.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import jakarta.validation.constraints.*;

public record ResourceSubTopicCreateRequest(
        @NotBlank @Size(max = 255) String name,
        @NotNull @PositiveOrZero Integer order,
        @NotNull ResourceType resourceType,
        @NotBlank @Size(max = 255) @Pattern(regexp = "^https?://.+$") String link) {}
