package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic extends IdAuditEntity {

    @Column(name = "title", nullable = false)
    @Getter
    private String title;

    @Column(name = "content")
    @Getter
    private String content;

    @Column(name = "importance_level", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    private ImportanceLevel importanceLevel;

    @Column(name = "sort_order", nullable = false)
    @Getter
    private Integer order;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "is_draft", nullable = false)
    @Getter
    private boolean isDraft;

    @Column(name = "is_deleted", nullable = false)
    @Getter
    private boolean isDeleted;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "road_map_id", nullable = false)
    private RoadMap roadMap;

    @OneToMany(
            mappedBy = "topic",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("order ASC")
    private List<ResourceTopic> resources = new ArrayList<>();

    @OneToMany(
            mappedBy = "topic",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<SubTopic> subTopics = new ArrayList<>();

    private Topic(
            String title,
            String content,
            ImportanceLevel importanceLevel,
            Integer order,
            boolean isDraft,
            List<ResourceTopic> resources,
            List<SubTopic> subTopics) {
        this.title = title;
        this.content = content;
        this.importanceLevel = importanceLevel;
        this.order = order;
        this.isDraft = isDraft;
        this.resources = resources;
        this.subTopics = subTopics;

        this.deletedAt = null;
        this.isDeleted = false;
        this.roadMap = null;
    }

    static Topic create(CreationTopic creationSpec) {

        String title = creationSpec.title();
        if (title.isBlank() || title.length() < 2 || 255 < title.length()) {
            throw new IllegalArgumentException("Topic.create: title 이 blank 이거나, 길이가 2 미만 255 초과");
        }

        String content = creationSpec.content();
        if (content != null && content.length() > 1000) {
            throw new IllegalArgumentException("Topic.create: content 길이가 1000 초과");
        }

        Integer order = creationSpec.order();
        if (order < 1) {
            throw new IllegalArgumentException("Topic.create: order 가 1 미만");
        }

        List<ResourceTopic> createdResourceTopics =
                creationSpec.creationResourceTopics().stream()
                        .map(ResourceTopic::create)
                        .sorted(Comparator.comparing(ResourceTopic::getOrder))
                        .toList();

        for (int i = 0; i < createdResourceTopics.size(); i++) {
            if (createdResourceTopics.get(i).getOrder() != (i + 1)) {
                throw new IllegalArgumentException(
                        "Topic.create: ResourceTopic 리스트 요소의 order 는 1부터 size 까지 1씩 증가해야 합니다.");
            }
        }

        List<SubTopic> createdSubTopics =
                creationSpec.creationSubTopics().stream().map(SubTopic::create).toList();

        Set<String> titleSet = new HashSet<>();
        for (SubTopic subTopic : createdSubTopics) {
            titleSet.add(subTopic.getTitle());
        }
        if (titleSet.size() != createdSubTopics.size()) {
            throw new IllegalArgumentException(
                    "Topic.create: Topic 에 속한 SubTopic 의 title 은 고유해야 합니다.");
        }

        Topic created =
                new Topic(
                        title,
                        content,
                        creationSpec.importanceLevel(),
                        order,
                        creationSpec.isDraft(),
                        createdResourceTopics,
                        createdSubTopics);

        // 양방향 연결
        created.resources.forEach(resource -> resource.setTopic(created));
        created.subTopics.forEach(subTopic -> subTopic.setTopic(created));

        return created;
    }

    void setRoadMap(RoadMap roadMap) {
        this.roadMap = Objects.requireNonNull(roadMap, "Topic.setRoadMap: 파라미터 roadMap 이 null 입니다");
    }

    public Timestamp getDeletedAt() {
        if (deletedAt == null) {
            return null;
        }
        return new Timestamp(deletedAt.getTime());
    }

    public List<ResourceTopic> getResources() {
        return List.copyOf(resources);
    }

    public List<SubTopic> getSubTopics() {
        return List.copyOf(subTopics);
    }

    void stepBack() {
        this.order++;
    }
}
