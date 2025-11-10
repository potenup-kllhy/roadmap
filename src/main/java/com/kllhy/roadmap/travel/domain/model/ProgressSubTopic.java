package com.kllhy.roadmap.travel.domain.model;

import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgressSubTopic extends IdAuditEntity {

    @Getter private Long subTopicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_topic_id", nullable = false)
    private ProgressTopic topic;

    private ProgressSubTopic(ProgressTopic topic, Long subTopicId) {
        this.topic = Objects.requireNonNull(topic, "topic must not be null");
        this.subTopicId = Objects.requireNonNull(subTopicId, "subTopicId must not be null");
    }

    static ProgressSubTopic create(ProgressTopic topic, Long subTopicId) {
        return new ProgressSubTopic(topic, subTopicId);
    }

    void setTopic(ProgressTopic topic) {
        this.topic = Objects.requireNonNull(topic, "topic must not be null");
    }

    void changeStatus(ProgressStatus status) {
        this.status = status;
    }
}
