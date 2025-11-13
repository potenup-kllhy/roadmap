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
    private final TopicJpaRepository topicJpaRepository;
    private final SubTopicJpaRepository subTopicJpaRepository;

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
    public RoadMap findByIdWithAssociations(long id) {
        RoadMap roadMap =
                roadMapJpaRepository
                        .findWithTopics(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException("RoadMap not found: " + id));

        topicJpaRepository.findAllWithResourcesByRoadMapId(id);
        topicJpaRepository.findAllWithSubTopicsByRoadMapId(id);
        subTopicJpaRepository.findAllWithResourceSubTopicsByRoadMapId(id);
        return roadMap;
    }
}
