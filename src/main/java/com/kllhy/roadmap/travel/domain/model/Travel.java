package com.kllhy.roadmap.travel.domain.model;

import com.kllhy.roadmap.common.model.AggregateRoot;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
public class Travel extends AggregateRoot {

    @Column(nullable = false, updatable = false)
    @Getter
    private Long userId;

    @Column(nullable = false)
    @Getter
    private Long roadMapId;

    @OneToMany(
            mappedBy = "travel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ProgressTopic> topics = new ArrayList<>();

    protected Travel() {}
}
