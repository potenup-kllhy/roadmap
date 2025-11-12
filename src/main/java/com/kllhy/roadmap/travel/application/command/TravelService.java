package com.kllhy.roadmap.travel.application.command;

import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;

public interface TravelService {
    public void create(Long userId, Long roadmapId);

    public TravelView update(TravelUpdateRequest request);
}
