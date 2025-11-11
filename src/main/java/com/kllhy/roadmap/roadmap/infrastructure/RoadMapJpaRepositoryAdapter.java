package com.kllhy.roadmap.roadmap.infrastructure;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.roadmap.domain.repository.RoadMapRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoadMapJpaRepositoryAdapter implements RoadMapRepository {
    private final RoadMapJpaRepository roadMapJpaRepository;

    @Override
    public Optional<RoadMap> findById(long id) {
        return roadMapJpaRepository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return roadMapJpaRepository.existsById(id);
    }
}
