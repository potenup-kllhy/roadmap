package com.kllhy.roadmap.roadmap.application.command.dto;

import java.util.List;

public record CreateRoadMapCommand(
        long userId,
        String title,
        String description,
        boolean isDraft,
        long categoryId,
        long userId,
        List<CreateTopicCommand> topics) {}
