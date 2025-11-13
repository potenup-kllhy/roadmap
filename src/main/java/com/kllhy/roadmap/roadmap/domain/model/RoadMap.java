package com.kllhy.roadmap.roadmap.domain.model;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.roadmap.domain.event.RoadMapEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.SubTopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.TopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.roadmap.domain.event.listener.RoadMapEntityListener;
import com.kllhy.roadmap.roadmap.domain.exception.RoadMapIErrorCode;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationRoadMap;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationTopic;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateRoadMap;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "road_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(RoadMapEntityListener.class)
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

    @OneToMany(mappedBy = "roadMap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
                        .collect(Collectors.toList());
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

        return created;
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

        // 로드맵 업데이트 이벤트 트리거
        roadMapUpdated();
    }

    private void updateTopics(UpdateRoadMap updateSpec) {

        Map<Long, Topic> wouldBeRemovedTopic =
                topics.stream()
                        .filter(topic -> topic.getId() != null)
                        .collect(Collectors.toMap(Topic::getId, topic -> topic));

        updateSpec
                .updateTopics()
                .forEach(
                        spec -> {
                            if (spec.id() != null) {
                                Topic existingTopic = wouldBeRemovedTopic.remove(spec.id());
                                if (existingTopic == null) {
                                    throw new IllegalArgumentException(
                                            "RoadMap.update: 존재하지 않는 Topic id 입니다.");
                                }
                                existingTopic.update(spec);
                            }
                            topics.add(Topic.create(spec));
                        });

        topics.forEach(Topic::softDelete);
        topics.sort(Comparator.comparing(Topic::getOrder));
        validateTopics(topics);

        // 역방향 연결
        topics.forEach(topic -> topic.setRoadMap(this));
    }

    private void triggerTopicEvent(Topic topic) {
        if (topic.isDeletedEventAvailable()) {
            addDomainEvent(
                    new TopicEventOccurred(
                            id,
                            topic.getId(),
                            EventType.DELETED,
                            topic.isDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE));

            return;
        }

        if (topic.isUpdatedEventAvailable()) {
            addDomainEvent(
                    new TopicEventOccurred(
                            id,
                            topic.getId(),
                            EventType.UPDATED,
                            topic.isDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE));
        }

        for (SubTopic subTopic : topic.getSubTopics()) {
            triggerSubTopicEvent(subTopic);
        }
    }

    private void triggerSubTopicEvent(SubTopic subTopic) {
        EventType eventType = null;
        // update flag 는 delete flag 에 덮어 씌워짐
        // 두 flag 를 한 번에 모두 읽어야 함
        if (subTopic.isUpdatedEventAvailable()) {
            eventType = EventType.UPDATED;
        }
        if (subTopic.isDeletedEventAvailable()) {
            eventType = EventType.DELETED;
        }
        addDomainEvent(
                new SubTopicEventOccurred(
                        id,
                        subTopic.getTopic().getId(),
                        subTopic.getId(),
                        eventType,
                        subTopic.getIsDraft() ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE));
    }

    private void roadMapUpdated() {
        addDomainEvent(
                new RoadMapEventOccurred(
                        id,
                        EventType.UPDATED,
                        isDraft ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE));

        for (Topic topic : topics) {
            triggerTopicEvent(topic);
        }
    }

    private void roadMapDeleted() {
        addDomainEvent(
                new RoadMapEventOccurred(
                        id,
                        EventType.DELETED,
                        isDraft ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE));
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

        int cur = 0;
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

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = Timestamp.from(Instant.now());
        roadMapDeleted();
    }

    public RoadMap cloneAsIs(long userId) {
        if (isDraft || isDeleted) {
            throw new DomainException(RoadMapIErrorCode.ROAD_MAP_CLONE_NOT_ALLOWED);
        }

        List<CreationTopic> topics = new ArrayList<>();
        int order = 1;
        for (Topic topic : this.topics) {
            if (topic.isCloneable()) {
                CreationTopic clonedTopic = topic.cloneAsIs(order++);
                topics.add(clonedTopic);
            }
        }

        CreationRoadMap clonedRoadMap =
                new CreationRoadMap(title, description, isDraft, categoryId, userId, topics);
        return RoadMap.create(clonedRoadMap);
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
