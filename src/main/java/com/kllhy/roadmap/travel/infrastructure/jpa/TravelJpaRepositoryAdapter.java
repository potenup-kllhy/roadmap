package com.kllhy.roadmap.travel.infrastructure.jpa;

import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TravelJpaRepositoryAdapter implements TravelRepository {

    private final TravelJpaRepository travelJpaRepository;

    @Override
    public Optional<Travel> findById(Long id) {
        return travelJpaRepository.findById(id);
    }

    @Override
    public Optional<Travel> findBatchById(Long id) {
        return travelJpaRepository.findBatchById(id);
    }

    @Override
    public Page<Travel> findBatchByUserId(Long userId, Pageable pageable) {
        return travelJpaRepository.findBatchByUserId(userId, pageable);
    }

    @Override
    public Optional<Travel> findBatchByRoadmapIdAndUserId(Long roadmapId, Long userId) {
        return travelJpaRepository.findBatchByRoadMapIdAndUserId(roadmapId, userId);
    }

    @Override
    public Travel save(Travel travel) {
        return travelJpaRepository.save(travel);
    }

    @Override
    public boolean existTravelById(Long id) {
        return travelJpaRepository.existsById(id);
    }
}
