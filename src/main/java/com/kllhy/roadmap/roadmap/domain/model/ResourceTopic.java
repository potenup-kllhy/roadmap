package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationResourceTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource_topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceTopic extends IdEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(name = "sort_order", nullable = false)
    @Getter
    private Integer order;

    // To Do: 안전한 링크인지 확인하는 기능도 있으면 괜찮을 것 같음
    @Column(name = "link", nullable = false)
    private String link;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    private ResourceTopic(String name, ResourceType resourceType, Integer order, String link) {
        this.name = name;
        this.resourceType = resourceType;
        this.order = order;
        this.link = link;

        this.topic = null;
    }

    public static ResourceTopic create(CreationResourceTopic creationSpec) {

        String name = creationSpec.name();
        if (name.isBlank() || name.length() < 2 || 255 < name.length()) {
            throw new IllegalArgumentException(
                    "ResourceTopic.create: name 이 blank 이거나, 길이가 2 미만 255 초과");
        }

        Integer order = creationSpec.order();
        if (order < 1) {
            throw new IllegalArgumentException("ResourceTopic.create: order 가 1 미만");
        }

        String link = creationSpec.link();
        if (link.isBlank() || 255 < link.length()) {
            throw new IllegalArgumentException(
                    "ResourceTopic.create: link 가 blank 이거나, 길이가 255 초과");
        }

        return new ResourceTopic(name, creationSpec.resourceType(), order, link);
    }

    void setTopic(Topic topic) {
        this.topic = Objects.requireNonNull(topic, "ResourceTopic.setTopic: 파라미터 topic 이 null 입니다");
    }
}
