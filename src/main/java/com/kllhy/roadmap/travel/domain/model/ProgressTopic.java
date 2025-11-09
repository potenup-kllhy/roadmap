package com.kllhy.roadmap.travel.domain.model;

import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProgressTopic extends IdAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    private Long topicId;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status = ProgressStatus.TODO;

    @OneToMany(
            mappedBy = "topic",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ProgressSubTopic> subTopics = new ArrayList<>();

    protected ProgressTopic() {}
}
