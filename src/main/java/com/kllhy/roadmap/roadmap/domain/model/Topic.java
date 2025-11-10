package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topic")
public class Topic extends IdAuditEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "importance_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportanceLevel importanceLevel;

    @Column(name = "sort_order", nullable = false)
    private Integer order;

    @Column(name = "deleted_at", nullable = false)
    private Timestamp deletedAt;

    @Column(name = "is_draft", nullable = false)
    private boolean isDraft;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "road_map_id", nullable = false)
    private RoadMap roadMap;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("order ASC")
    private List<ResourceTopic> resources = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SubTopic> subTopics = new ArrayList<>();

    protected Topic() {
    }

    private Topic(String title, String content, ImportanceLevel importanceLevel, Integer order, boolean isDraft, List<ResourceTopic> resources, List<SubTopic> subTopics) {
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

    public static Topic create(CreationTopic creationSpec) {
        // To Do: Topic 생성자 불변식 검증

        Topic created = new Topic(
                creationSpec.title(),
                creationSpec.content(),
                creationSpec.importanceLevel(),
                creationSpec.order(),
                creationSpec.isDraft(),
                creationSpec.resources(),
                creationSpec.subTopics()
        );

        // 양방향 연결
        created.resources.forEach(resource -> resource.setTopic(created));
        created.subTopics.forEach(subTopic -> subTopic.setTopic(created));

        return created;
    }

    void setRoadMap(RoadMap roadMap) {
        if (roadMap == null) {
            // To Do: 나중에 도메인 예외 발생시키도록 변경
            throw new RuntimeException("Topic.setRoadMap: 파라미터 roadMap 이 null 입니다");
        }
        this.roadMap = roadMap;
    }
}
