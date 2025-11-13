package com.kllhy.roadmap.star.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRoadMapJpaRepository extends JpaRepository<StarRoadMap, Long> {
    List<StarRoadMap> findByUserId(Long userId);

    List<StarRoadMap> findByRoadMapId(Long roadmapId);

    void deleteByUserIdAndRoadMapId(Long userId, Long roadmapId);

    void deleteStarRoadMapsByUserId(Long userId);

    void deleteStarRoadMapsByRoadMapId(Long roadmapId);

    boolean existsByUserIdAndRoadMapId(Long userId, Long roadmapId);
}
