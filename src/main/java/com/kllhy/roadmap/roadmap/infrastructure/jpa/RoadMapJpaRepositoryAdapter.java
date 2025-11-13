package com.kllhy.roadmap.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.roadmap.domain.repository.RoadMapRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public long save(RoadMap roadMap) {
        return roadMapJpaRepository.save(roadMap).getId();
    }

    @Override
    public boolean existsById(long id) {
        return roadMapJpaRepository.existsById(id);
    }

    @Override
    public Optional<RoadMap> findByIdWithAssociations(long id) {
        return roadMapJpaRepository.findByIdWithAssociations(id);
    }
}
