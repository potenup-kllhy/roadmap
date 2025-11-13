package com.kllhy.roadmap.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoadMapJpaRepository extends JpaRepository<RoadMap, Long> {

    @EntityGraph(
            attributePaths = {
                "topics",
                "topics.resources",
                "topics.subTopics",
                "topics.subTopics.resources"
            })
    @Query("select r from RoadMap r where r.id = :id")
    Optional<RoadMap> findByIdWithAssociations(@Param("id") Long id);
}
