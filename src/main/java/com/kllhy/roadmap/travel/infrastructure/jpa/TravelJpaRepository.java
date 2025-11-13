package com.kllhy.roadmap.travel.infrastructure.jpa;

import com.kllhy.roadmap.travel.domain.model.Travel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TravelJpaRepository extends JpaRepository<Travel, Long> {

    @EntityGraph(value = "travel.withTopics", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select t from Travel t where t.id = :id")
    Optional<Travel> findBatchById(@Param("id") Long id);

    @EntityGraph(value = "travel.withTopics", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select t from Travel t where t.userId = :userId")
    Page<Travel> findBatchByUserId(@Param("userId") Long userId, Pageable pageable);

    @EntityGraph(value = "travel.withTopics", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select t from Travel t where t.roadMapId = :roadmapId and t.userId = :userId")
    Optional<Travel> findBatchByRoadMapIdAndUserId(
            @Param("roadmapId") Long roadmapId, @Param("userId") Long userId);

    List<Travel> findAllByRoadMapId(Long roadmapId);

    @Query("select t from Travel t join fetch ProgressTopic pt where pt.topicId = :id")
    List<Travel> findAllByTopicId(Long topicId);

    @Query(
            """
    select distinct t.id
    from Travel t
    join t.topics pt
    join pt.subTopics pst
    where pst.subTopicId in :ids
    """)
    List<Long> findTravelIdsBySubTopicIds(@Param("ids") Collection<Long> ids);

    @EntityGraph(value = "travel.withTopics", type = EntityGraph.EntityGraphType.LOAD)
    List<Travel> findByIdIn(Collection<Long> ids);
}
