package com.kllhy.roadmap.roadmap.domain.model;

import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationRoadMap;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.*;

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

        String title = creationSpec.title();
        if (title.isBlank() || title.length() < 2 || 255 < title.length()) {
            throw new IllegalArgumentException("RoadMap.create: title 이 blank 이거나, 길이가 2 미만 또는 255 초과");
        }

        String description = creationSpec.description();
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("RoadMap.create: description 의 길이가 1000 초과");
        }

        List<Topic> createdTopics = creationSpec.creationTopics() == null
                ? Collections.emptyList()
                : creationSpec.creationTopics()
                        .stream()
                        .map(Topic::create)
                        .sorted(Comparator.comparing(Topic::getOrder))
                        .toList();

        for (int i = 0; i < createdTopics.size(); i++) {
            if (createdTopics.get(i).getOrder() != (i + 1)) {
                throw new IllegalArgumentException("RoadMap.create: Topic 리스트 요소의 order 는 1부터 size 까지 1씩 증가해야 합니다.");
            }
        }

        Set<String> titleSet = new HashSet<>();
        for (Topic topic : createdTopics) {
            titleSet.add(topic.getTitle());
        }
        if (titleSet.size() != createdTopics.size()) {
            throw new IllegalArgumentException("RoadMap.create: RoadMap 에 속한 Topic 의 title 은 고유해야 합니다.");
        }

        RoadMap created =
                new RoadMap(
                        title,
                        description,
                        creationSpec.isDraft(),
                        creationSpec.categoryId(),
                        createdTopics);

        // 양방향 연결
        created.topics.forEach(topic -> topic.setRoadMap(created));

        return created;
    }
}
