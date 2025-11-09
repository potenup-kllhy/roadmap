package com.kllhy.roadmap.travel.application.query;

import com.kllhy.roadmap.travel.application.query.dto.TravelView;

public interface TravelQueryService {

    public TravelView getById(Long travelId);
}
