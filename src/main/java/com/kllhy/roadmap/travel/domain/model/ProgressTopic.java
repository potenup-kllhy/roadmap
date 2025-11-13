package com.kllhy.roadmap.travel.domain.model;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgressTopic extends IdAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Getter private Long topicId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.TODO;

    @OneToMany(
            mappedBy = "topic",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private List<ProgressSubTopic> subTopics = new ArrayList<>();

    @Getter
    @Column(nullable = false)
    private boolean isArchived = false;

    private ProgressTopic(Travel travel, Long topicId) {
        this.travel = Objects.requireNonNull(travel, "travel must not be null");
        this.topicId = Objects.requireNonNull(topicId, "topicId must not be null");
    }

    static ProgressTopic create(Travel travel, Long topicId) {
        return new ProgressTopic(travel, topicId);
    }

    public List<ProgressSubTopic> getSubTopics() {
        return List.copyOf(subTopics);
    }

    void addSubTopics(List<ProgressSubTopicCommand> commands) {
        updateValid();
        if (commands == null || commands.isEmpty()) return;

        for (var command : commands) {
            addSubTopic(ProgressSubTopic.create(this, command.subTopicId()));
        }
    }

    private void addSubTopic(ProgressSubTopic sub) {
        boolean exists =
                subTopics.stream()
                        .anyMatch(s -> Objects.equals(s.getSubTopicId(), sub.getSubTopicId()));
        if (exists) throw new DomainException(TravelErrorCode.TRAVEL_SUB_TOPICS_DUPLICATED);

        sub.setTopic(this);
        subTopics.add(sub);
    }

    void setTravel(Travel travel) {
        this.travel = Objects.requireNonNull(travel, "travel must not be null");
    }

    void changeStatus(ProgressStatus status) {
        this.status = status;
    }

    void markSubTopic(Long subTopicId, ProgressStatus status) {
        updateValid();
        ProgressSubTopic subTopic = getSubTopicOrThrow(subTopicId);
        subTopic.changeStatus(status);
    }

    private void updateValid() {
        if (isArchived) throw new DomainException(TravelErrorCode.TRAVEL_TOPICS_NOT_FOUND);
    }

    public ProgressSubTopic getSubTopicOrThrow(Long subTopicId) {
        return subTopics.stream()
                .filter(st -> st.getSubTopicId().equals(subTopicId))
                .findFirst()
                .orElseThrow(
                        () -> new DomainException(TravelErrorCode.TRAVEL_SUB_TOPICS_NOT_FOUND));
    }

    public void activate(ActiveStatus status) {
        Objects.requireNonNull(status, "status");
        this.isArchived = status.equals(ActiveStatus.INACTIVE);
    }
}
