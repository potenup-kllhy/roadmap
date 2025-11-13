package com.kllhy.roadmap.roadmap.application.query;

import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;

public interface RoadMapQueryService {
    RoadMapView findById(Long id);

    boolean existsById(Long id);

    RoadMapView findByIdWithAssociations(long id);
}
