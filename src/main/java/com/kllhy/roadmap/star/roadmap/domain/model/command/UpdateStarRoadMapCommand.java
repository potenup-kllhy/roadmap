package com.kllhy.roadmap.star.roadmap.domain.model.command;

import java.util.Objects;

public record UpdateStarRoadMapCommand(Long userId, Long starRoadMapId, int value) {
    public UpdateStarRoadMapCommand {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(starRoadMapId, "starRoadMapId must not be null");
    }
}
