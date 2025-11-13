package com.kllhy.roadmap.star.roadmap.domain.model.command;

import java.util.Objects;

public record UpdateStarRoadMapCommand(Long userId, Long starId, int newValue) {
    public UpdateStarRoadMapCommand {
        Objects.requireNonNull(starId, "UpdateStarRoadMap: star_roadmapId is null");
        Objects.requireNonNull(userId, "UpdateStarRoadMap: userId is null");
    }
}
