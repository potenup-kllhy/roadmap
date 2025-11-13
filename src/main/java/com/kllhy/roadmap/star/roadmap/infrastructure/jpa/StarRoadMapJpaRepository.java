package com.kllhy.roadmap.star.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRoadMapJpaRepository extends JpaRepository<StarRoadMap, Long> {

    List<StarRoadMap> findStarRoadMapByUserId(Long userId);

    List<StarRoadMap> findStarRoadMapByRoadMapId(Long roadmapId);

    void deleteStarRoadMapsByUserId(Long userId);

    void deleteStarRoadMapsByRoadMapId(Long roadmapId);

    void deleteByUserIdAndRoadMapId(Long userId, Long roadmapId);

    boolean existsByUserIdAndRoadMapId(Long userId, Long roadmapId);
}
