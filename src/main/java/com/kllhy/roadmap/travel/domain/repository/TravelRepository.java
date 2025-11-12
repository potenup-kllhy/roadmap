package com.kllhy.roadmap.travel.domain.repository;

import com.kllhy.roadmap.travel.domain.model.Travel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TravelRepository {

    Optional<Travel> findById(Long id);

    Optional<Travel> findBatchById(Long id);

    Page<Travel> findBatchByUserId(Long userId, Pageable pageable);

    Optional<Travel> findBatchByRoadmapIdAndUserId(Long roadmapId, Long userId);

    Travel save(Travel travel);

    boolean existTravelById(Long id);
}
