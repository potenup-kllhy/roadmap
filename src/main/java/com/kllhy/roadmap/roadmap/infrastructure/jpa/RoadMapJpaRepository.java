package com.kllhy.roadmap.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadMapJpaRepository extends JpaRepository<RoadMap, Long> {

    @EntityGraph(
            attributePaths = {
                "topics",
                "topics.resources",
                "topics.subTopics",
                "topics.subTopics.resources"
            })
    Optional<RoadMap> findByIdWithAssociations(Long id);
}
