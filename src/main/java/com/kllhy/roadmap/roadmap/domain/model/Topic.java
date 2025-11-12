package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateResourceTopic;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateTopic;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
        validateTitle(title);

        String content = creationSpec.content();
        validateContent(content);

        Integer order = creationSpec.order();
        validateOrder(order);

        List<ResourceTopic> createdResourceTopics = creationSpec.creationResourceTopics().stream()
                .map(ResourceTopic::create)
                .sorted(Comparator.comparing(ResourceTopic::getOrder))
                .toList();
        validateResources(createdResourceTopics);

        List<SubTopic> createdSubTopics = creationSpec.creationSubTopics().stream()
                .map(SubTopic::create).toList();
        validateSubTopics(createdSubTopics);

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

    /** id 사용 x **/
    static Topic create(UpdateTopic updateSpec) {

        validateTitle(updateSpec.title());
        validateContent(updateSpec.content());
        validateOrder(updateSpec.order());

        List<ResourceTopic> createdResourceTopics = updateSpec.updateResourceTopics().stream()
                .map(ResourceTopic::create)
                .sorted(Comparator.comparing(ResourceTopic::getOrder))
                .toList();
        validateResources(createdResourceTopics);

        List<SubTopic> createdSubTopics = updateSpec.updateSubTopics()
                .stream().map(SubTopic::create).toList();
        validateSubTopics(createdSubTopics);

        Topic created =
                new Topic(
                        updateSpec.title(),
                        updateSpec.content(),
                        updateSpec.importanceLevel(),
                        updateSpec.order(),
                        updateSpec.isDraft(),
                        createdResourceTopics,
                        createdSubTopics);

        // 양방향 연결
        created.resources.forEach(resource -> resource.setTopic(created));
        created.subTopics.forEach(subTopic -> subTopic.setTopic(created));

        return created;
    }

    void update(UpdateTopic updateSpec) {
        validateTitle(updateSpec.title());
        validateContent(updateSpec.content());
        validateOrder(updateSpec.order());

        this.title = updateSpec.title();
        this.content = updateSpec.content();
        this.importanceLevel = updateSpec.importanceLevel();
        this.order = updateSpec.order();
        this.isDraft = updateSpec.isDraft();

        // resources 동기화
        updateResources(updateSpec);

        // subtopic 동기화
        updateSubTopics(updateSpec);
    }

    private void updateResources(UpdateTopic updateSpec) {
        Map<Long, ResourceTopic> remainingResources = resources.stream()
                .filter(resource -> resource.getId() != null)
                .collect(Collectors.toMap(ResourceTopic::getId, resource -> resource));

        List<ResourceTopic> sortedUpdatedResources = updateSpec.updateResourceTopics().stream()
                .sorted(Comparator.comparing(UpdateResourceTopic::order))
                .map(spec -> {
                    if (spec.id() != null) {
                        ResourceTopic existing = remainingResources.remove(spec.id());
                        if (existing == null) {
                            throw new IllegalArgumentException(
                                    "Topic.update: 존재하지 않는 ResourceTopic id 입니다.");
                        }
                        existing.update(spec);
                        return existing;
                    }
                    return ResourceTopic.create(spec);
                })
                .toList();

        validateResources(sortedUpdatedResources);

        // 역방향 연결
        sortedUpdatedResources.forEach(resource -> resource.setTopic(this));
        resources.clear();
        resources.addAll(sortedUpdatedResources);
    }

    private void updateSubTopics(UpdateTopic updateSpec) {
        Map<Long, SubTopic> remainingSubTopics = subTopics.stream()
                .filter(subTopic -> subTopic.getId() != null)
                .collect(Collectors.toMap(SubTopic::getId, subTopic -> subTopic));

        List<SubTopic> updatedSubTopics = new ArrayList<>();
        updateSpec.updateSubTopics().stream()
                .map(spec -> {
                    if (spec.id() != null) {
                        SubTopic existing = remainingSubTopics.remove(spec.id());
                        if (existing == null) {
                            throw new IllegalArgumentException(
                                    "Topic.update: 존재하지 않는 SubTopic id 입니다.");
                        }
                        existing.update(spec);
                        return existing;
                    }
                    return SubTopic.create(spec);
                })
                .forEach(updatedSubTopics::add);

        validateSubTopics(updatedSubTopics);

        // 역방향 연결
        updatedSubTopics.forEach(subTopic -> subTopic.setTopic(this));
        subTopics.clear();
        subTopics.addAll(updatedSubTopics);
    }

    private static void validateTitle(String title) {
        if (title.isBlank() || title.length() < 2 || 255 < title.length()) {
            throw new IllegalArgumentException("Topic.validateTitle: title 이 blank 이거나, 길이가 2 미만 255 초과");
        }
    }

    private static void validateContent(String content) {
        if (content != null && content.length() > 1000) {
            throw new IllegalArgumentException("Topic.validateContent: content 길이가 1000 초과");
        }
    }

    private static void validateOrder(Integer order) {
        if (order < 1) {
            throw new IllegalArgumentException("Topic.validateOrder: order 가 1 미만");
        }
    }

    private static void validateResources(List<ResourceTopic> resources) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).getOrder() != (i + 1)) {
                throw new IllegalArgumentException(
                        "Topic.validateResources: ResourceTopic 리스트 요소의 order 는 1부터 size 까지 1씩 증가해야 합니다.");
            }
        }
    }

    private static void validateSubTopics(List<SubTopic> subTopics) {
        Set<String> titleSet = new HashSet<>();
        for (SubTopic subTopic : subTopics) {
            titleSet.add(subTopic.getTitle());
        }
        if (titleSet.size() != subTopics.size()) {
            throw new IllegalArgumentException(
                    "Topic.validateSubTopics: Topic 에 속한 SubTopic 의 title 은 고유해야 합니다.");
        }
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
