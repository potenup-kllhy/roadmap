package com.kllhy.roadmap.roadmap.application.command.dto;

import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import java.util.List;

public record CreateTopicCommand(
        String title,
        String content,
        ImportanceLevel importanceLevel,
        int order,
        boolean isDraft,
        List<CreateResourceTopicCommand> resourceTopics,
        List<CreateSubTopicCommand> subTopics) {}
