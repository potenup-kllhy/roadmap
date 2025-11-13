package com.kllhy.roadmap.roadmap.application.command.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;

public record CreateResourceTopicCommand(
        String name, ResourceType resourceType, int order, String link) {}
