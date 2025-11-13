package com.kllhy.roadmap.star.roadmap.domain.repository;

import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import java.util.List;
import java.util.Optional;

public interface StarRoadMapRepository {
    StarRoadMap save(StarRoadMap starRoadMap);

    Optional<StarRoadMap> findById(Long id);

    List<StarRoadMap> findByUserId(Long userId);

    List<StarRoadMap> findByRoadmapId(Long roadmapId);

    List<StarRoadMap> findAll();

    void deleteById(Long id);

    void deleteAllByUserId(Long userId);

    void deleteAllByRoadmapId(Long roadmapId);

    void deleteByUserIdAndRoadmapId(Long userId, Long roadmapId);

    boolean existsByUserIdAndRoadmapId(Long userId, Long roadmapId);
}
