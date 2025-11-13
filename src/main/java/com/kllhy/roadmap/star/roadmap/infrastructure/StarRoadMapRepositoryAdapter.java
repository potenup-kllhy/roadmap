package com.kllhy.roadmap.star.roadmap.infrastructure;

import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import com.kllhy.roadmap.star.roadmap.infrastructure.jpa.StarRoadMapJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StarRoadMapRepositoryAdapter implements StarRoadMapRepository {

    private final StarRoadMapJpaRepository starRoadMapJpaRepository;

    @Override
    public StarRoadMap save(StarRoadMap starRoadMap) {
        return starRoadMapJpaRepository.save(starRoadMap);
    }

    @Override
    public Optional<StarRoadMap> findById(Long id) {
        return starRoadMapJpaRepository.findById(id);
    }

    @Override
    public List<StarRoadMap> findByUserId(Long userId) {
        return starRoadMapJpaRepository.findStarRoadMapByUserId(userId);
    }

    @Override
    public List<StarRoadMap> findByRoadmapId(Long roadmapId) {
        return starRoadMapJpaRepository.findStarRoadMapByRoadMapId(roadmapId);
    }

    @Override
    public Optional<StarRoadMap> findByUserIdAndRoadmapId(Long userId, Long roadmapId) {
        return starRoadMapJpaRepository.findByUserIdAndRoadMapId(userId, roadmapId);
    }

    @Override
    public void deleteByUserIdAndRoadmapId(Long userId, Long roadmapId) {
        starRoadMapJpaRepository.deleteByUserIdAndRoadMapId(userId, roadmapId);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        starRoadMapJpaRepository.deleteStarRoadMapsByUserId(userId);
    }

    @Override
    public void deleteAllByRoadmapId(Long roadmapId) {
        starRoadMapJpaRepository.deleteStarRoadMapsByRoadMapId(roadmapId);
    }

    @Override
    public boolean existsByUserIdAndRoadmapId(Long userId, Long roadmapId) {
        return starRoadMapJpaRepository.existsByUserIdAndRoadMapId(userId, roadmapId);
    }
}
