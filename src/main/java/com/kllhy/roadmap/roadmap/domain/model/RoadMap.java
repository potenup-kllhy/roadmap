package com.kllhy.roadmap.roadmap.domain.model;

import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationRoadMap;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationTopic;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateRoadMap;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateTopic;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "road_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadMap extends AggregateRoot {

    @Column(name = "title", nullable = false)
    @Getter
    private String title;

    @Column(name = "description")
    @Getter
    private String description;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "is_deleted", nullable = false)
    @Getter
    private boolean isDeleted;

    @Column(name = "is_draft", nullable = false)
    @Getter
    private boolean isDraft;

    @Column(name = "category_id", nullable = false)
    @Getter
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
        validateTitle(title);

        String description = creationSpec.description();
        validateDescription(description);

        List<Topic> createdTopics =
                creationSpec.creationTopics().stream()
                        .map(Topic::create)
                        .sorted(Comparator.comparing(Topic::getOrder))
                        .toList();
        validateTopics(createdTopics);

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

    public void update(UpdateRoadMap updateSpec) {
        Objects.requireNonNull(updateSpec, "RoadMap.update: updateSpec 이 null 입니다.");

        validateTitle(updateSpec.title());
        validateDescription(updateSpec.description());
        validateCategoryId(updateSpec);

        this.title = updateSpec.title();
        this.description = updateSpec.description();
        this.isDraft = updateSpec.isDraft();
        this.categoryId = updateSpec.categoryId();

        updateTopics(updateSpec);
    }

    private void updateTopics(UpdateRoadMap updateSpec) {
        Map<Long, Topic> remainingTopics = topics.stream()
                .filter(topic -> topic.getId() != null)
                .collect(Collectors.toMap(Topic::getId, topic -> topic));

        List<Topic> sortedUpdatedTopics = updateSpec.updateTopics().stream()
                .sorted(Comparator.comparing(UpdateTopic::order))
                .map(spec -> {
                    if (spec.id() != null) {
                        Topic existing = remainingTopics.remove(spec.id());
                        if (existing == null) {
                            throw new IllegalArgumentException("RoadMap.update: 존재하지 않는 Topic id 입니다.");
                        }
                        existing.update(spec);
                        return existing;
                    }
                    return Topic.create(spec);
                })
                .toList();

        validateTopics(sortedUpdatedTopics);

        // 역방향 연결
        sortedUpdatedTopics.forEach(topic -> topic.setRoadMap(this));
        topics.clear();
        topics.addAll(sortedUpdatedTopics);
    }

    private static void validateTitle(String title) {
        if (title.isBlank() || title.length() < 2 || 255 < title.length()) {
            throw new IllegalArgumentException(
                    "RoadMap.create: title 이 blank 이거나, 길이가 2 미만 또는 255 초과");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("RoadMap.create: description 의 길이가 1000 초과");
        }
    }

    private static void validateCategoryId(UpdateRoadMap updateSpec) {
        if (updateSpec.categoryId() < 0) {
            throw new IllegalArgumentException("RoadMap.update: categoryId 가 음수입니다.");
        }
    }

    private static void validateTopics(List<Topic> createdTopics) {
        if (createdTopics.isEmpty()) {
            throw new IllegalArgumentException("RoadMap.create: creationTopics 가 blank 임");
        }

        for (int i = 0; i < createdTopics.size(); i++) {
            if (createdTopics.get(i).getOrder() != (i + 1)) {
                throw new IllegalArgumentException(
                        "RoadMap.create: Topic 리스트 요소의 order 는 1부터 size 까지 1씩 증가해야 합니다.");
            }
        }

        Set<String> titleSet = new HashSet<>();
        for (Topic topic : createdTopics) {
            titleSet.add(topic.getTitle());
        }
        if (titleSet.size() != createdTopics.size()) {
            throw new IllegalArgumentException(
                    "RoadMap.create: RoadMap 에 속한 Topic 의 title 은 고유해야 합니다.");
        }
    }

    public Timestamp getDeletedAt() {
        if (deletedAt == null) {
            return null;
        }
        return new Timestamp(deletedAt.getTime());
    }

    public List<Topic> getTopics() {
        return List.copyOf(topics);
    }

    public void insertTopic(CreationTopic creationTopic) {

        if (creationTopic == null) {
            throw new IllegalArgumentException("RoadMap.insertTopic: creationTopic 이 null 입니다.");
        }

        int desiredOrder = creationTopic.order();

        if (desiredOrder < 1 || (topics.size() + 1) < desiredOrder) {
            throw new IllegalArgumentException("RoadMap.insertTopic: desiredOrder 가 유효한 순서 범위에서 벗어났습니다.");
        }

        if (isTitleDuplicated(creationTopic)) {
            throw new IllegalArgumentException("RoadMap.insertTopic: RoadMap 내에서 Topic 의 title 은 유일해야 합니다.");
        }

        int insertAt = desiredOrder - 1;
        Topic toBeInserted = Topic.create(creationTopic);
        if (insertAt == (topics.size())) {
            topics.add(toBeInserted);
        } else {
            stepBackEachOrderFrom(insertAt);
            topics.add(insertAt, toBeInserted);
        }

        toBeInserted.setRoadMap(this);
    }

    private boolean isTitleDuplicated(CreationTopic creationTopic) {
        return topics.stream().anyMatch(topic -> topic.getTitle().equals(creationTopic.title()));
    }

    private void stepBackEachOrderFrom(int thisPoint) {
        for (int i = thisPoint; i < topics.size(); i++) {
            topics.get(i).stepBack();
        }
    }
}
