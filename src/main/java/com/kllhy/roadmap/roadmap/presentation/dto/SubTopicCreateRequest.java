package com.kllhy.roadmap.roadmap.presentation.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record SubTopicCreateRequest(
        @NotBlank @Size(max = 255) String title,
        @Size(max = 255) String content,
        @NotNull ImportanceLevel importanceLevel,
        boolean isDraft,
        @Valid List<ResourceSubTopicCreateRequest> resources) {}
