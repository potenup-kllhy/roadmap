package com.kllhy.roadmap.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoadMapJpaRepository extends JpaRepository<RoadMap, Long> {

    @Query(
            """
        select distinct r from RoadMap r
        left join fetch r.topics t
        where r.id = :id
        """)
    Optional<RoadMap> findWithTopics(@Param("id") Long id);
}
