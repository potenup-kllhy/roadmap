package com.kllhy.roadmap.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.roadmap.domain.model.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicJpaRepository extends JpaRepository<Topic, Long> {
    @Query(
            """
        select distinct t from Topic t
        left join fetch t.resources rt
        where t.roadMap.id = :roadMapId
        """)
    List<Topic> findAllWithResourcesByRoadMapId(@Param("roadMapId") Long roadMapId);

    @Query(
            """
        select distinct t from Topic t
        left join fetch t.subTopics st
        where t.roadMap.id = :roadMapId
        """)
    List<Topic> findAllWithSubTopicsByRoadMapId(@Param("roadMapId") Long roadMapId);
}
