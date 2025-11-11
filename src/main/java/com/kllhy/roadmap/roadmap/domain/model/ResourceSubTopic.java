package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationResourceSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource_sub_topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceSubTopic extends IdEntity {

    @Column(nullable = false)
    @Getter
    private String name;

    @Column(name = "sort_order", nullable = false)
    @Getter
    private Integer order;

    @Column(name = "resource_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Getter
    private ResourceType resourceType;

    @Column(nullable = false)
    @Getter
    private String link;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "sub_topic_id", nullable = false)
    private SubTopic subTopic;

    private ResourceSubTopic(String name, Integer order, ResourceType resourceType, String link) {
        this.name = name;
        this.order = order;
        this.resourceType = resourceType;
        this.link = link;

        this.subTopic = null;
    }

    static ResourceSubTopic create(CreationResourceSubTopic creationSpec) {
        String name = creationSpec.name();
        if (name.isBlank() || name.length() < 2 || 255 < name.length()) {
            throw new IllegalArgumentException(
                    "ResourceSubTopic.create: name 이 blank 이거나, 길이가 2 미만 또는 255 초과");
        }

        Integer order = creationSpec.order();
        if (order < 1) {
            throw new IllegalArgumentException("ResourceSubTopic.create: order 가 1 미만");
        }

        String link = creationSpec.link();
        if (link.isBlank() || 255 < link.length()) {
            throw new IllegalArgumentException(
                    "ResourceSubTopic.create: link 가 blank 이거나, 길이가 255 초과");
        }

        return new ResourceSubTopic(name, order, creationSpec.resourceType(), link);
    }

    void setSubTopic(SubTopic subTopic) {
        this.subTopic =
                Objects.requireNonNull(
                        subTopic, "ResourceSubTopic.setSubTopic: 파라미터 subTopic 이 null 입니다");
    }
}
