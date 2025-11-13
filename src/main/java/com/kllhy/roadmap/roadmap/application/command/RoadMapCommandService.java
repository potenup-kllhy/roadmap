package com.kllhy.roadmap.roadmap.application.command;

import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateRoadMap;

public interface RoadMapCommandService {
    void update(long roadMapId, UpdateRoadMap updateRoadMap);
}
