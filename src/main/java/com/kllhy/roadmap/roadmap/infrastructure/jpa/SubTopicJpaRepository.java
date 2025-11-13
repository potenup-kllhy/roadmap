package com.kllhy.roadmap.roadmap.infrastructure.jpa;

import com.kllhy.roadmap.roadmap.domain.model.SubTopic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubTopicJpaRepository extends JpaRepository<SubTopic, Long> {
    @Query(
            """
        select distinct st from SubTopic st
        left join fetch st.resources srt
        where st.topic.roadMap.id = :roadMapId
        """)
    List<SubTopic> findAllWithResourceSubTopicsByRoadMapId(@Param("roadMapId") Long roadMapId);
}
