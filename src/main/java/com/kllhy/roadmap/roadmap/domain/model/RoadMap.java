package com.kllhy.roadmap.roadmap.domain.model;

import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationRoadMap;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "road_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadMap extends AggregateRoot {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "is_draft", nullable = false)
    private boolean isDraft;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @OneToMany(
            mappedBy = "roadMap",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Topic> topics = new ArrayList<>();

    private RoadMap(
            String title,
            String description,
            boolean isDraft,
            Long categoryId,
            List<Topic> topics) {
        this.title = title;
        this.description = description;
        this.isDraft = isDraft;
        this.categoryId = categoryId;
        this.topics = topics;

        this.deletedAt = null;
        this.isDeleted = false;
    }

    public static RoadMap create(CreationRoadMap creationSpec) {
        // To Do: RoadMap 생성자 불변식 검증

        List<Topic> createdTopics = creationSpec.creationTopics()
                .stream()
                .map(Topic::create)
                .sorted(Comparator.comparing(Topic::getOrder))
                .toList();

        RoadMap created =
                new RoadMap(
                        creationSpec.title(),
                        creationSpec.description(),
                        creationSpec.isDraft(),
                        creationSpec.categoryId(),
                        createdTopics);

        // 양방향 연결
        created.topics.forEach(topic -> topic.setRoadMap(created));

        return created;
    }
}
