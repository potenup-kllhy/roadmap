package com.kllhy.roadmap.star.roadmap.application.query;

import com.kllhy.roadmap.star.roadmap.application.query.dto.StarRoadMapView;
import java.util.List;

public interface StarRoadMapQueryService {
    StarRoadMapView getById(Long starRoadMapId);

    List<StarRoadMapView> getAllStarByUserId(Long userId);

    List<StarRoadMapView> getAllStarByRoadMapId(Long roadMapId);
}
