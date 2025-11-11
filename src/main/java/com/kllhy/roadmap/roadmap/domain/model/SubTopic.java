package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "sub_topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubTopic extends IdAuditEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "content", nullable = true)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportanceLevel importanceLevel;

    @Column(nullable = false)
    private Timestamp deletedAt;

    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToMany(
            mappedBy = "subTopic",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("order ASC")
    private List<ResourceSubTopic> resources = new ArrayList<>();

    public SubTopic(
            String title,
            String content,
            ImportanceLevel importanceLevel,
            Boolean isDraft,
            List<ResourceSubTopic> resources) {
        this.title = title;
        this.content = content;
        this.importanceLevel = importanceLevel;
        this.isDraft = isDraft;
        this.resources = resources;

        this.deletedAt = null;
        this.isDeleted = false;
        this.topic = null;
    }

    public static SubTopic create(CreationSubTopic creationSpec) {
        // To Do: SubTopic 생성자 불변식 검증

        List<ResourceSubTopic> createdResourceSubTopics = creationSpec.creationResourceSubTopics()
                .stream()
                .map(ResourceSubTopic::create)
                .sorted(Comparator.comparing(ResourceSubTopic::getOrder))
                .toList();

        SubTopic created =
                new SubTopic(
                        creationSpec.title(),
                        creationSpec.content(),
                        creationSpec.importanceLevel(),
                        creationSpec.isDraft(),
                        createdResourceSubTopics);

        // 양방향 연결
        created.resources.forEach(resource -> resource.setSubTopic(created));

        return created;
    }

    void setTopic(Topic topic) {
        if (topic == null) {
            // To Do: 나중에 도메인 예외 발생시키도록 변경
            throw new RuntimeException("SubTopic.setTopic: 파라미터 topic 이 null 입니다");
        }
        this.topic = topic;
    }
}
