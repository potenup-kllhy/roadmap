package com.kllhy.roadmap.travel.application.query;

import com.kllhy.roadmap.travel.application.view.TravelView;

public interface TravelQueryService {

    public TravelView getById(Long travelId);
}
