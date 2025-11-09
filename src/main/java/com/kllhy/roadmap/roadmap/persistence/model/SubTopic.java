package com.kllhy.roadmap.roadmap.persistence.model;

import com.kllhy.roadmap.roadmap.persistence.model.enums.ImportanceLevel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_topic")
public class SubTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportanceLevel importanceLevel;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp modifiedAt;

    @Column(nullable = false)
    private Timestamp deletedAt;

    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "subtopic_id", nullable = false)
    @OrderBy("order ASC")
    private List<ResourceSubTopic> resources = new ArrayList<>();
}
