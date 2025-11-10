package com.kllhy.roadmap.roadmap.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kllhy.roadmap.common.model.IdEntity;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.CreationResourceSubTopic;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_sub_topic")
public class ResourceSubTopic extends IdEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "sort_order", nullable = false)
    private Integer order;

    @Enumerated(value = EnumType.STRING)
    private ResourceType resourceType;

    @Column(nullable = false)
    private String link;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "sub_topic_id", nullable = false)
    private SubTopic subTopic;

    protected ResourceSubTopic() {
    }

    private ResourceSubTopic(String name, Integer order, ResourceType resourceType, String link) {
        this.name = name;
        this.order = order;
        this.resourceType = resourceType;
        this.link = link;

        this.subTopic = null;
    }

    public static ResourceSubTopic create(CreationResourceSubTopic creationSpec) {
        // To Do: ResourceSubTopic 생성자 불변식 검증
        return new ResourceSubTopic(
                creationSpec.name(),
                creationSpec.order(),
                creationSpec.resourceType(),
                creationSpec.link()
        );
    }

    void setSubTopic(SubTopic subTopic) {
        if (subTopic == null) {
            // To Do: 나중에 도메인 예외 발생시키도록 변경
            throw new RuntimeException("ResourceSubTopic.setSubTopic: 파라미터 subTopic 이 null 입니다");
        }
        this.subTopic = subTopic;
    }
}
