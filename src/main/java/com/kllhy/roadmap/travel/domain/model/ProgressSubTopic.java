package com.kllhy.roadmap.travel.domain.model;

import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import jakarta.persistence.*;

@Entity
public class ProgressSubTopic extends IdAuditEntity {

    private Long subTopicId;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status = ProgressStatus.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_topic_id", nullable = false)
    private ProgressTopic topic;

    protected ProgressSubTopic() {}
}
