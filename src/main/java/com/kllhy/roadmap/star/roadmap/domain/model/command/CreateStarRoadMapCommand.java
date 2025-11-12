package com.kllhy.roadmap.star.roadmap.domain.model.command;

import java.util.Objects;

public record CreateStarRoadMapCommand(Long userId, Long roadmapId, int value) {

    public CreateStarRoadMapCommand {
        Objects.requireNonNull(roadmapId, "CreationStarRoadMap: roadmapId is null");
        Objects.requireNonNull(userId, "CreationStarRoadMap: userId is null");
    }
}
