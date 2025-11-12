package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateResourceSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateSubTopic;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubTopic extends IdAuditEntity {

    @Column(nullable = false)
    @Getter
    private String title;

    @Column(name = "content")
    @Getter
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    private ImportanceLevel importanceLevel;

    private Timestamp deletedAt;

    @Column(name = "is_draft", nullable = false)
    @Getter
    private Boolean isDraft;

    @Column(name = "is_deleted", nullable = false)
    @Getter
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

    static SubTopic create(CreationSubTopic creationSpec) {

        String title = creationSpec.title();
        validateTitle(title);

        String content = creationSpec.content();
        validateContent(content);

        List<ResourceSubTopic> createdResourceSubTopics =
                creationSpec.creationResourceSubTopics().stream()
                        .map(ResourceSubTopic::create)
                        .sorted(Comparator.comparing(ResourceSubTopic::getOrder))
                        .toList();
        validateResources(createdResourceSubTopics);

        SubTopic created =
                new SubTopic(
                        title,
                        creationSpec.content(),
                        creationSpec.importanceLevel(),
                        creationSpec.isDraft(),
                        createdResourceSubTopics);

        // 양방향 연결
        created.resources.forEach(resource -> resource.setSubTopic(created));

        return created;
    }

    /** id 사용 x **/
    static SubTopic create(UpdateSubTopic updateSpec) {
        validateTitle(updateSpec.title());
        validateContent(updateSpec.content());

        List<ResourceSubTopic> createdResourceSubTopics = updateSpec.updateResourceSubTopics()
                .stream()
                .map(ResourceSubTopic::create)
                .sorted(Comparator.comparing(ResourceSubTopic::getOrder))
                .toList();
        validateResources(createdResourceSubTopics);

        SubTopic created =
                new SubTopic(
                        updateSpec.title(),
                        updateSpec.content(),
                        updateSpec.importanceLevel(),
                        updateSpec.isDraft(),
                        createdResourceSubTopics);

        // 양방향 연결
        created.resources.forEach(resource -> resource.setSubTopic(created));

        return created;
    }

    void update(UpdateSubTopic updateSpec) {
        validateTitle(updateSpec.title());
        validateContent(updateSpec.content());

        this.title = updateSpec.title();
        this.content = updateSpec.content();
        this.importanceLevel = updateSpec.importanceLevel();
        this.isDraft = updateSpec.isDraft();

        Map<Long, ResourceSubTopic> remainingResources = resources.stream()
                .filter(resource -> resource.getId() != null)
                .collect(Collectors.toMap(ResourceSubTopic::getId, resource -> resource));

        List<ResourceSubTopic> sortedUpdatedResources = updateSpec.updateResourceSubTopics()
                .stream()
                .sorted(Comparator.comparing(UpdateResourceSubTopic::order))
                .map(spec -> {
                    if (spec.id() != null) {
                        ResourceSubTopic existing = remainingResources.remove(spec.id());
                        if (existing == null) {
                            throw new IllegalArgumentException(
                                    "SubTopic.update: 존재하지 않는 ResourceSubTopic id 입니다.");
                        }
                        existing.update(spec);
                        return existing;
                    }
                    return ResourceSubTopic.create(spec);
                })
                .toList();

        validateResources(sortedUpdatedResources);

        // 양방향 연결
        sortedUpdatedResources.forEach(resource -> resource.setSubTopic(this));

        resources.clear();
        resources.addAll(sortedUpdatedResources);
    }

    private static void validateTitle(String title) {
        if (title.isBlank() || title.length() < 2 || 255 < title.length()) {
            throw new IllegalArgumentException(
                    "SubTopic.create: title 이 blank 이거나, 길이가 2 미만 또는 255 초과");
        }
    }

    private static void validateContent(String content) {
        if (content != null && content.length() > 1000) {
            throw new IllegalArgumentException("SubTopic.create: content 길이가 1000 초과");
        }
    }

    private static void validateResources(List<ResourceSubTopic> createdResourceSubTopics) {
        for (int i = 0; i < createdResourceSubTopics.size(); i++) {
            if (createdResourceSubTopics.get(i).getOrder() != (i + 1)) {
                throw new IllegalArgumentException(
                        "SubTopic.create: ResourceSubTopic 리스트 요소의 order 는 1부터 size 까지 1씩 증가해야 합니다.");
            }
        }
    }

    void setTopic(Topic topic) {
        this.topic = Objects.requireNonNull(topic, "SubTopic.setTopic: 파라미터 topic 이 null 입니다");
    }

    public Timestamp getDeletedAt() {
        if (deletedAt == null) {
            return null;
        }
        return new Timestamp(deletedAt.getTime());
    }

    public List<ResourceSubTopic> getResources() {
        return List.copyOf(resources);
    }
}
