package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationResourceSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateResourceSubTopic;
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
        validateName(name);

        Integer order = creationSpec.order();
        validateOrder(order);

        String link = creationSpec.link();
        validateLink(link);

        return new ResourceSubTopic(name, order, creationSpec.resourceType(), link);
    }

    /** id 사용 x **/
    static ResourceSubTopic create(UpdateResourceSubTopic updateSpec) {
        return create(new CreationResourceSubTopic(
                updateSpec.name(), updateSpec.order(), updateSpec.resourceType(), updateSpec.link()));
    }

    void update(UpdateResourceSubTopic updateSpec) {
        validateName(updateSpec.name());
        validateOrder(updateSpec.order());
        validateLink(updateSpec.link());

        this.name = updateSpec.name();
        this.order = updateSpec.order();
        this.resourceType = updateSpec.resourceType();
        this.link = updateSpec.link();
    }

    private static void validateName(String name) {
        if (name.isBlank() || name.length() < 2 || 255 < name.length()) {
            throw new IllegalArgumentException(
                    "ResourceSubTopic.create: name 이 blank 이거나, 길이가 2 미만 또는 255 초과");
        }
    }

    private static void validateOrder(Integer order) {
        if (order < 1) {
            throw new IllegalArgumentException("ResourceSubTopic.create: order 가 1 미만");
        }
    }

    private static void validateLink(String link) {
        if (link.isBlank() || 255 < link.length()) {
            throw new IllegalArgumentException(
                    "ResourceSubTopic.create: link 가 blank 이거나, 길이가 255 초과");
        }
    }

    void setSubTopic(SubTopic subTopic) {
        this.subTopic =
                Objects.requireNonNull(
                        subTopic, "ResourceSubTopic.setSubTopic: 파라미터 subTopic 이 null 입니다");
    }
}
