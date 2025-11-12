package com.kllhy.roadmap.travel.domain.model;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "travel.withTopics", attributeNodes = @NamedAttributeNode("topics"))
@Table(
        name = "travel",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_travel_user_roadmap",
                    columnNames = {"user_id", "road_map_id"})
        })
public class Travel extends AggregateRoot {

    @Getter
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Getter
    @Column(nullable = false)
    private Long roadMapId;

    @OneToMany(
            mappedBy = "travel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private final List<ProgressTopic> topics = new ArrayList<>();

    @Getter
    @Column(nullable = false)
    private boolean isInvalid = false;

    private Travel(Long userId, Long roadMapId) {
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.roadMapId = Objects.requireNonNull(roadMapId, "roadMapId must not be null");
    }

    public static Travel create(TravelCommand command) {
        Travel travel = new Travel(command.userId(), command.roadmapId());
        travel.addTopics(command.topics());
        return travel;
    }

    public List<ProgressTopic> getTopics() {
        return List.copyOf(topics);
    }

    private void addTopics(List<ProgressTopicCommand> commands) {
        if (commands == null || commands.isEmpty())
            throw new DomainException(TravelErrorCode.TRAVEL_TOPICS_INVALID);

        for (var command : commands) {
            var topic = ProgressTopic.create(this, command.topicId());
            topic.addSubTopics(command.subTopics());
            addTopic(topic);
        }
    }

    public void markTopic(Long topicId, ProgressStatus status) {
        Objects.requireNonNull(status, "status");
        ProgressTopic topic = getTopicOrThrow(topicId);
        topic.changeStatus(status);
    }

    public void markSubTopic(Long topicId, Long subTopicId, ProgressStatus status) {
        Objects.requireNonNull(status, "status");
        ProgressTopic topic = getTopicOrThrow(topicId);
        topic.markSubTopic(subTopicId, status);
    }

    private void addTopic(ProgressTopic topic) {
        boolean exists =
                topics.stream().anyMatch(t -> Objects.equals(t.getTopicId(), topic.getTopicId()));
        if (exists) throw new DomainException(TravelErrorCode.TRAVEL_TOPICS_DUPLICATED);

        topic.setTravel(this);
        topics.add(topic);
    }

    private ProgressTopic getTopicOrThrow(Long topicId) {
        return topics.stream()
                .filter(t -> t.getTopicId().equals(topicId))
                .findFirst()
                .orElseThrow(() -> new DomainException(TravelErrorCode.TRAVEL_TOPICS_NOT_FOUND));
    }

    public TravelProgressStatus getStatus() {
        return calculateStatus();
    }

    private TravelProgressStatus calculateStatus() {
        Set<ProgressStatus> allStatuses =
                topics.stream()
                        .flatMap(
                                topic ->
                                        Stream.concat(
                                                Stream.of(topic.getStatus()),
                                                topic.getSubTopics().stream()
                                                        .map(ProgressSubTopic::getStatus)))
                        .collect(Collectors.toSet());

        if (allStatuses.contains(ProgressStatus.TODO)
                || allStatuses.contains(ProgressStatus.IN_PROGRESS)) {
            return TravelProgressStatus.IN_PROGRESS;
        }

        return allStatuses.contains(ProgressStatus.DONE)
                ? TravelProgressStatus.DONE
                : TravelProgressStatus.IN_PROGRESS;
    }
}
