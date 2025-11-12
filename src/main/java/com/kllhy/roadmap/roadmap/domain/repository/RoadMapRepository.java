package com.kllhy.roadmap.roadmap.domain.repository;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import java.util.Optional;

public interface RoadMapRepository {
    Optional<RoadMap> findById(long id);

    boolean existsById(long id);
}
