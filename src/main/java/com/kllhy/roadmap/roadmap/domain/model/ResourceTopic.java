package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationResourceTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import jakarta.persistence.*;
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
        // To Do: ResourceTopic 생성자 불변식 검증

        return new ResourceTopic(
                creationSpec.name(),
                creationSpec.resourceType(),
                creationSpec.order(),
                creationSpec.link());
    }

    void setTopic(Topic topic) {
        if (topic == null) {
            // To Do: 나중에 도메인 예외 발생시키도록 변경
            throw new RuntimeException("ResourceTopic.setTopic: 파라미터 topic 이 null 입니다");
        }
        this.topic = topic;
    }
}
