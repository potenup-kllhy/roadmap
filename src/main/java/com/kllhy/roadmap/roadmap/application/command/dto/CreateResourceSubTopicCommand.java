package com.kllhy.roadmap.roadmap.application.command.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;

public record CreateResourceSubTopicCommand(
        String name, int order, ResourceType resourceType, String link) {}
