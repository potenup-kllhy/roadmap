package com.kllhy.roadmap.roadmap.domain.model;

import com.kllhy.roadmap.roadmap.domain.model.creation_spec.*;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RoadMapDomainTest {
    @Test
    @DisplayName("로드맵 생성에 성공한다")
    void create_success() {
        // given
        var creationRoadMap = testRoadMapBuilders(successTestTopicBuilders());

        // when
        RoadMap rm = RoadMap.create(creationRoadMap);

        // then
        assertThat(rm.getTitle()).isEqualTo("Spring Backend Roadmap");
        assertThat(rm.getDescription()).isEqualTo("Spring Boot, JPA, REST 설계 중심 로드맵");
        assertThat(rm.isDraft()).isFalse();
        assertThat(rm.getCategoryId()).isEqualTo(1L);

        assertThat(rm.getTopics()).hasSize(3);
        assertThat(rm.getTopics()).extracting(Topic::getOrder).containsExactly(1, 2, 3);
        assertThat(rm.getTopics()).extracting(Topic::isDraft).containsExactly(false, false, true);

        assertThat(rm.getTopics().get(0).getSubTopics()).hasSize(1);
        assertThat(rm.getTopics().get(1).getSubTopics()).hasSize(2);
        assertThat(rm.getTopics().get(2).getSubTopics()).hasSize(3);

        var testSubTopic = rm.getTopics().get(0).getSubTopics().get(0);
        assertThat(testSubTopic.getTitle()).isEqualTo("프로젝트 스캐폴딩");
        assertThat(testSubTopic.getContent()).isEqualTo("start.spring.io 사용");
        assertThat(testSubTopic.getImportanceLevel()).isEqualTo( ImportanceLevel.DEFAULT);
        assertThat(testSubTopic.getIsDraft()).isEqualTo(true);
        assertThat(testSubTopic.getResources()).hasSize(1);

        assertThat(rm.getTopics().get(0).getResources()).hasSize(2);
    }

    @Test
    @DisplayName("토픽이 없는 로드맵 생성에 실패한다")
    void failToCreate_WhenNoTopics() {
        // given
        var creationRoadMap = testRoadMapBuilders(List.of());

        // when & then
        assertThatThrownBy(() -> RoadMap.create(creationRoadMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("creationTopics 가 blank 임");
    }

    @Test
    @DisplayName("로드맵 클론에 성공한다")
    void clone_success() {
        // given
        var creationRoadMap = testRoadMapBuilders(successTestTopicBuilders());
        RoadMap rm = RoadMap.create(creationRoadMap);

        // when
        RoadMap clonedRoadMap = rm.cloneAsIs(1);

        // then
        clonedRoadMap.getTopics().forEach(t -> {
            assertThat(rm.isDraft()).isFalse();
            t.getSubTopics().forEach(s -> assertThat(s.getIsDraft()).isFalse());
        });

        assertThat(clonedRoadMap.getTopics()).hasSize(2);
        assertThat(clonedRoadMap.getTopics().get(1).getSubTopics()).hasSize(1);
    }


    static CreationRoadMap testRoadMapBuilders(List<CreationTopic> topics) {
        return new CreationRoadMap(
                "Spring Backend Roadmap",
                "Spring Boot, JPA, REST 설계 중심 로드맵",
                false,
                1L,
                1L,
                topics
        );
    }

    static List<CreationTopic> successTestTopicBuilders() {
        return List.of(
                new CreationTopic(
                        "개발 환경",
                        "JDK, Gradle, IDE 세팅",
                        ImportanceLevel.RECOMMENDED,
                        1,
                        false,
                        List.of(
                                new CreationResourceTopic("Adoptium Temurin", ResourceType.OFFICIAL, 1, "https://adoptium.net/"),
                                new CreationResourceTopic("Gradle User Guide", ResourceType.OFFICIAL, 2, "https://docs.gradle.org/current/userguide/userguide.html")
                        ),
                        List.of(
                                new CreationSubTopic(
                                        "프로젝트 스캐폴딩",
                                        "start.spring.io 사용",
                                        ImportanceLevel.DEFAULT,
                                        true,
                                        List.of(
                                                new CreationResourceSubTopic("Spring Initializr", 1, ResourceType.OFFICIAL, "https://start.spring.io/")
                                        )
                                )
                        )
                ),
                new CreationTopic(
                        "REST API 설계",
                        "리소스 모델과 상태코드, 오류 응답",
                        ImportanceLevel.RECOMMENDED,
                        2,
                        false,
                        List.of(
                                new CreationResourceTopic("RFC 9110 HTTP Semantics", ResourceType.OFFICIAL, 1, "https://www.rfc-editor.org/rfc/rfc9110"),
                                new CreationResourceTopic("Richardson Maturity Model", ResourceType.POST, 2, "https://martinfowler.com/articles/richardsonMaturityModel.html")
                        ),
                        List.of(
                                new CreationSubTopic(
                                        "에러 응답 규격",
                                        "Problem+JSON 적용",
                                        ImportanceLevel.DEFAULT,
                                        false,
                                        List.of(
                                                new CreationResourceSubTopic("RFC 7807 Problem Details", 1, ResourceType.OFFICIAL, "https://www.rfc-editor.org/rfc/rfc7807")
                                        )
                                ),
                                new CreationSubTopic(
                                        "페이징과 정렬",
                                        "Cursor vs Offset",
                                        ImportanceLevel.OPTIONAL,
                                        true,
                                        List.of(
                                                new CreationResourceSubTopic("Cursor Pagination 사례", 1, ResourceType.POST, "https://shopify.engineering/pagination-relative-cursors")
                                        )
                                )
                        )
                ),
                new CreationTopic(
                        "JPA와 영속성",
                        "엔티티 매핑, 트랜잭션, 성능",
                        ImportanceLevel.RECOMMENDED,
                        3,
                        true,
                        List.of(
                                new CreationResourceTopic("Hibernate User Guide", ResourceType.OFFICIAL, 1, "https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html"),
                                new CreationResourceTopic("JPA 사양", ResourceType.OFFICIAL, 2, "https://jakarta.ee/specifications/persistence/"),
                                new CreationResourceTopic("백엔드 로드맵 레퍼런스", ResourceType.ROADMAP, 3, "https://roadmap.sh/backend")
                        ),
                        List.of(
                                new CreationSubTopic(
                                        "지연 로딩과 N+1",
                                        "Fetch Join, Entity Graph",
                                        ImportanceLevel.RECOMMENDED,
                                        false,
                                        List.of(
                                                new CreationResourceSubTopic("N+1 해결 가이드", 1, ResourceType.POST, "https://vladmihalcea.com/n-plus-1-query-problem/")
                                        )
                                ),
                                new CreationSubTopic(
                                        "트랜잭션 경계",
                                        "서비스 계층에서의 경계 설정",
                                        ImportanceLevel.PARALLEL,
                                        false,
                                        List.of(
                                                new CreationResourceSubTopic("Spring Transaction 문서", 1, ResourceType.OFFICIAL, "https://docs.spring.io/spring-framework/reference/data-access/transaction.html"),
                                                new CreationResourceSubTopic("실전 트랜잭션 전파", 2, ResourceType.POST, "https://spring.io/guides/gs/managing-transactions/")
                                        )
                                ),
                                new CreationSubTopic(
                                        "엔티티 설계 원칙",
                                        "동등성, 식별자, 값 타입",
                                        ImportanceLevel.OPTIONAL,
                                        true,
                                        List.of(
                                                new CreationResourceSubTopic("DDD Aggregates 소개", 1, ResourceType.POST, "https://martinfowler.com/bliki/DDD_Aggregate.html"),
                                                new CreationResourceSubTopic("JPA 값 타입 정리", 2, ResourceType.POST, "https://www.baeldung.com/jpa-value-type")
                                        )
                                )
                        )
                )
        );
    }
}