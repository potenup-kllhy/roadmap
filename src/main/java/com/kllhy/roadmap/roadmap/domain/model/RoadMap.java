package com.kllhy.roadmap.roadmap.domain.model;

import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.roadmap.domain.event.RoadMapEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.SubTopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.TopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationRoadMap;
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

    @Column(name = "uuid", nullable = false, unique = true)
    @Getter
    private UUID uuid;

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
            UUID uuid,
            String title,
            String description,
            boolean isDraft,
            Long categoryId,
            List<Topic> topics) {
        this.uuid = uuid;
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
                        UUID.randomUUID(),
                        title,
                        description,
                        creationSpec.isDraft(),
                        creationSpec.categoryId(),
                        createdTopics);

        // 양방향 연결
        created.topics.forEach(topic -> topic.setRoadMap(created));

        // RoadMap Event
        addCreateOrUpdateEvents(created);

        return created;
    }

    private static void addCreateOrUpdateEvents(RoadMap created) {
        ActiveStatus roadMapActiveStatus = created.isDraft ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE;
        EventType roadMapEventType = created.getId() == null ? EventType.CREATED : EventType.UPDATED;
        created.addDomainEvent(new RoadMapEventOccurred(
                created.uuid, roadMapEventType, roadMapActiveStatus));

        for (Topic topic : created.topics) {
            // Topic Event
            ActiveStatus topicActiveStatus = topic.isDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE;
            EventType topicEventType = topic.getId() == null ? EventType.CREATED : EventType.UPDATED;
            created.addDomainEvent(new TopicEventOccurred(
                    created.uuid, topic.getUuid(), topicEventType, topicActiveStatus));

            // SubTopic Event
            for (SubTopic subTopic : topic.getSubTopics()) {
                ActiveStatus subTopicActiveStatus = subTopic.getIsDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE;
                EventType subTopicEventType = subTopic.getId() == null ? EventType.CREATED : EventType.UPDATED;
                created.addDomainEvent(new SubTopicEventOccurred(
                        created.uuid, topic.getUuid(), subTopic.getUuid(), EventType.CREATED, ActiveStatus.ACTIVE));
            }
        }
    }

    public void update(UpdateRoadMap updateSpec) {
        Objects.requireNonNull(updateSpec, "RoadMap.update: updateSpec 이 null 입니다.");

        validateTitle(updateSpec.title());
        validateDescription(updateSpec.description());
        validateCategoryId(updateSpec.categoryId());

        this.title = updateSpec.title();
        this.description = updateSpec.description();
        this.isDraft = updateSpec.isDraft();
        this.categoryId = updateSpec.categoryId();

        updateTopics(updateSpec);
    }

    private void updateTopics(UpdateRoadMap updateSpec) {
        Map<Long, Topic> remainingTopics =
                topics.stream()
                        .filter(topic -> topic.getId() != null)
                        .collect(Collectors.toMap(Topic::getId, topic -> topic));

        List<Topic> sortedUpdatedTopics = updateSpec.updateTopics().stream()
                .sorted(Comparator.comparing(UpdateTopic::order))
                .map(spec -> {
                        if (spec.id() != null) {
                            Topic existing = remainingTopics.remove(spec.id());
                            if (existing == null) {
                                throw new IllegalArgumentException(
                                        "RoadMap.update: 존재하지 않는 Topic id 입니다.");
                            }
                            existing.update(spec);
                            return existing;
                        }
                        return Topic.create(spec);
                })
                .toList();

        validateTopics(sortedUpdatedTopics);
        topics = sortedUpdatedTopics;

        // 역방향 연결
        topics.forEach(topic -> topic.setRoadMap(this));
    }

    private static void validateTitle(String title) {
        if (title.isBlank() || title.length() < 2 || 255 < title.length()) {
            throw new IllegalArgumentException(
                    "RoadMap.validateTitle: title 이 blank 이거나, 길이가 2 미만 또는 255 초과");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException(
                    "RoadMap.validateDescription: description 의 길이가 1000 초과");
        }
    }

    private static void validateCategoryId(Long categoryId) {
        if (categoryId < 0) {
            throw new IllegalArgumentException("RoadMap.validateCategoryId: categoryId 가 음수입니다.");
        }
    }

    private static void validateTopics(List<Topic> topics) {
        if (topics.isEmpty()) {
            throw new IllegalArgumentException("RoadMap.validateTopics: topics 가 blank 임");
        }

        for (int i = 0; i < topics.size(); i++) {
            if (topics.get(i).getOrder() != (i + 1)) {
                throw new IllegalArgumentException(
                        "RoadMap.validateTopics: Topic 리스트 요소의 order 는 1부터 size 까지 1씩 증가해야 합니다.");
            }
        }

        Set<String> titleSet = new HashSet<>();
        for (Topic topic : topics) {
            titleSet.add(topic.getTitle());
        }
        if (titleSet.size() != topics.size()) {
            throw new IllegalArgumentException(
                    "RoadMap.validateTopics: RoadMap 에 속한 Topic 의 title 은 고유해야 합니다.");
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
}
