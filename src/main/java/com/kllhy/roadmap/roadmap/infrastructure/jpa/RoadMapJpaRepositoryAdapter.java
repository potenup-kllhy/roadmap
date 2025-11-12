package com.kllhy.roadmap.roadmap.infrastructure.jpa;

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
        // TODO: 현재 LAZY 전략으로 전체 조회가 불가능한데, 추후 한번에 전체 조회 가능하도록 변경해 놓겠습니다
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

    public Optional<RoadMap> findByIdWithAssociations(long id) {
        return roadMapJpaRepository.findByIdWithAssociations(id);
    }
}
