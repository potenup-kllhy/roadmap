package com.kllhy.roadmap.travel.application.service.query;

import com.kllhy.roadmap.travel.application.view.TravelView;
import java.util.List;

public interface TravelQueryService {

    public TravelView getById(Long travelId);

    public List<TravelView> getByUser(Long userId);
}
