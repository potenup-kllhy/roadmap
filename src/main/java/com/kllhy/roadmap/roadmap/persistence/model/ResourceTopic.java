package com.kllhy.roadmap.roadmap.persistence.model;

import com.kllhy.roadmap.roadmap.persistence.model.enums.ResourceType;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_topic")
public class ResourceTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // To Do: ResourceTopic.name 제약 사항 결정(ex. 길이, not null?)
    @Column(name = "name")
    private String name;

    // To Do: 효진님 PR(#3) 머지 후 rebase 해서 ResourceType 열거형 가져와야 함, 그 전까지 커밋하지 말 것
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    // To Do: sort_order 제약 사항 결정(ex. 0이상? 아니면 1이상?, ...)
    @Column(name = "sort_order", nullable = false)
    private Integer order;

    // To Do: 안전한 링크인지 확인하는 기능도 있으면 괜찮을 것 같음
    @Column(name = "link", nullable = false)
    private String link;
}
