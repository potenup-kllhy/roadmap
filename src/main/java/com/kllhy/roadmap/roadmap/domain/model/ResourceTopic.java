package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationResourceTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateResourceTopic;
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
    @Getter
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    private ResourceType resourceType;

    @Column(name = "sort_order", nullable = false)
    @Getter
    private Integer order;

    // To Do: 안전한 링크인지 확인하는 기능도 있으면 괜찮을 것 같음
    @Column(name = "link", nullable = false)
    @Getter
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

    static ResourceTopic create(CreationResourceTopic creationSpec) {

        String name = creationSpec.name();
        validateName(name);

        Integer order = creationSpec.order();
        validateOrder(order);

        String link = creationSpec.link();
        validateLink(link);

        return new ResourceTopic(name, creationSpec.resourceType(), order, link);
    }

    /** id 사용 x * */
    static ResourceTopic create(UpdateResourceTopic updateSpec) {
        return create(
                new CreationResourceTopic(
                        updateSpec.name(),
                        updateSpec.resourceType(),
                        updateSpec.order(),
                        updateSpec.link()));
    }

    void update(UpdateResourceTopic updateSpec) {
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
                    "ResourceTopic.validateName: name 이 blank 이거나, 길이가 2 미만 255 초과");
        }
    }

    private static void validateOrder(Integer order) {
        if (order < 1) {
            throw new IllegalArgumentException("ResourceTopic.validateOrder: order 가 1 미만");
        }
    }

    private static void validateLink(String link) {
        if (link.isBlank() || 255 < link.length()) {
            throw new IllegalArgumentException(
                    "ResourceTopic.validateLink: link 가 blank 이거나, 길이가 255 초과");
        }
    }

    void setTopic(Topic topic) {
        this.topic = Objects.requireNonNull(topic, "ResourceTopic.setTopic: 파라미터 topic 이 null 입니다");
    }
}
