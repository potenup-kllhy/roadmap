package com.kllhy.roadmap.roadmap.persistence.model;

import com.kllhy.roadmap.roadmap.persistence.model.enums.ImportanceLevel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // To Do: N+1 문제 주의!
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private List<ResourceTopic> resources = new ArrayList<>();

    // To Do: Topic.title 제약 사항 결정(ex. 길이)
    @Column(name = "title", nullable = false)
    private String title;

    // To Do: Topic.content 제약 사항 결정(ex. 길이)
    @Column(name = "content")
    private String content;

    // To Do: 효진님 PR(#3) 머지 후 rebase 해서 ResourceType 열거형 가져와야 함, 그 전까지 커밋하지 말 것
    @Column(name = "importance_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportanceLevel importanceLevel;

    // To Do: Topic.orderInRoadMap 제약 사항 결정(ex. 0이상? 아니면 1이상?)
    @Column(name = "order_in_roadmap", nullable = false)
    private Integer order;

    // To Do: 시간 순서 제약 사항 createdAt(nn) -> modifiedAt(nullable) -> deletedAt(nullable)
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;

    @Column(name = "deleted_at", nullable = false)
    private Timestamp deletedAt;

    @Column(name = "is_draft", nullable = false)
    private boolean isDraft;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
