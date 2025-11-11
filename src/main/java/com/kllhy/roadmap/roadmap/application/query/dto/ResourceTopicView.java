package com.kllhy.roadmap.roadmap.application.query.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;

public record ResourceTopicView(
        long id, String name, ResourceType resourceType, int order, String link) {}
