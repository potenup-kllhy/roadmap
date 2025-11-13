package com.kllhy.roadmap.roadmap.application.command;

import com.kllhy.roadmap.roadmap.application.command.dto.CreateRoadMapCommand;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateRoadMap;

public interface RoadMapCommandService {
    long createRoadMap(CreateRoadMapCommand command);

    void update(long roadMapId, UpdateRoadMap updateRoadMap);

    long cloneRoadMap(long userId, long roadMapId, long categoryId);
}
