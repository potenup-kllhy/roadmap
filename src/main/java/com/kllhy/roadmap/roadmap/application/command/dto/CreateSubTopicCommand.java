package com.kllhy.roadmap.roadmap.application.command.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.util.List;

public record CreateSubTopicCommand(
        String title,
        String content,
        ImportanceLevel importanceLevel,
        boolean isDraft,
        List<CreateResourceSubTopicCommand> resourceSubTopics) {}
