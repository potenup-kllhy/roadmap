package com.kllhy.roadmap.star.roadmap.application.command;

import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;

public interface StarRoadMapCommandService {
    Long create(CreateStarRoadMapCommand command);

    void update(UpdateStarRoadMapCommand command);

    void deleteById(Long starId);

    void deleteAllStarByUserId(Long userId);

    void deleteAllStarByRoadMapId(Long roadmapId);

    void deleteByUserIdAndRoadmapId(Long userId, Long roadmapId);
}
