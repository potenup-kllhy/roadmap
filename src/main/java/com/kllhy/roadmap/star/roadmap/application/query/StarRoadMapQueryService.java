package com.kllhy.roadmap.star.roadmap.application.query;

import com.kllhy.roadmap.star.roadmap.application.query.dto.StarRoadMapView;
import java.util.List;

public interface StarRoadMapQueryService {
    StarRoadMapView getById(Long starRoadMapId);

    List<StarRoadMapView> getStarByUserId(Long userId);

    List<StarRoadMapView> getStarByRoadMapId(Long roadMapId);
}
