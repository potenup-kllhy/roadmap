package com.kllhy.roadmap.roadmap.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kllhy.roadmap.common.RestDocsSupport;
import com.kllhy.roadmap.roadmap.application.command.RoadMapCommandService;
import com.kllhy.roadmap.roadmap.application.command.dto.CreateRoadMapCommand;
import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.ResourceSubTopicView;
import com.kllhy.roadmap.roadmap.application.query.dto.ResourceTopicView;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.application.query.dto.SubTopicView;
import com.kllhy.roadmap.roadmap.application.query.dto.TopicView;
import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
import com.kllhy.roadmap.roadmap.presentation.dto.ResourceSubTopicCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.ResourceTopicCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.RoadMapCloneRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.RoadMapCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.SubTopicCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.TopicCreateRequest;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class RoadMapControllerTest extends RestDocsSupport {

    @MockitoBean private RoadMapCommandService roadMapCommandService;

    @MockitoBean private RoadMapQueryService roadMapQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("신규 로드맵을 생성한다.")
    @Test
    void createRoadMap() throws Exception {
        // given
        long newRoadMapId = 2L;
        RoadMapCreateRequest request = createSampleRoadmapRequest();
        given(roadMapCommandService.createRoadMap(any(CreateRoadMapCommand.class)))
                .willReturn(newRoadMapId);

        // when & then
        mockMvc.perform(
                        post("/api-v1/roadmaps")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentCreateRoadMap());
    }

    @DisplayName("기존 로드맵을 복제한다.")
    @Test
    void cloneRoadMap() throws Exception {
        // given
        long originalRoadMapId = 1L;
        long clonedRoadMapId = 3L;
        RoadMapCloneRequest request = new RoadMapCloneRequest(1L, 2L);

        given(
                        roadMapCommandService.cloneRoadMap(
                                originalRoadMapId, request.userId(), request.categoryId()))
                .willReturn(clonedRoadMapId);

        // when & then
        mockMvc.perform(
                        post("/api-v1/roadmaps/{id}/clone", originalRoadMapId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentCloneRoadMap());
    }

    @DisplayName("ID로 로드맵 상세 정보를 조회한다.")
    @Test
    void getRoadMapById() throws Exception {
        // given
        long roadMapId = 1L;
        RoadMapView sampleView = createSampleRoadmapView(roadMapId);
        given(roadMapQueryService.findByIdWithAssociations(anyLong())).willReturn(sampleView);

        // when & then
        mockMvc.perform(
                        get("/api-v1/roadmaps/{id}", roadMapId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentGetRoadMapById());
    }

    private RoadMapCreateRequest createSampleRoadmapRequest() {
        ResourceSubTopicCreateRequest resourceSubTopicCreateRequest =
                new ResourceSubTopicCreateRequest(
                        "서브 토픽 리소스 이름",
                        0,
                        ResourceType.VIDEO,
                        "https://example.com/subtopic-resource");

        SubTopicCreateRequest subTopicCreateRequest =
                new SubTopicCreateRequest(
                        "서브 토픽 제목",
                        "서브 토픽 내용",
                        ImportanceLevel.RECOMMENDED,
                        false,
                        List.of(resourceSubTopicCreateRequest));

        ResourceTopicCreateRequest resourceTopicCreateRequest =
                new ResourceTopicCreateRequest(
                        "토픽 리소스 이름", ResourceType.POST, 1, "https://example.com/topic-resource");

        TopicCreateRequest topicCreateRequest =
                new TopicCreateRequest(
                        "토픽 제목",
                        "토픽 설명",
                        ImportanceLevel.DEFAULT,
                        1,
                        false,
                        List.of(resourceTopicCreateRequest),
                        List.of(subTopicCreateRequest));

        return new RoadMapCreateRequest(
                1L, "새로운 로드맵", "로드맵 설명", false, 1L, List.of(topicCreateRequest));
    }

    private RoadMapView createSampleRoadmapView(long roadMapId) {
        long categoryId = 1L;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        ResourceSubTopicView resourceSubTopicView =
                new ResourceSubTopicView(
                        101L,
                        "서브 토픽 리소스 뷰 이름",
                        ResourceType.OFFICIAL,
                        0,
                        "https://example.com/subtopic-resource-view");

        SubTopicView subTopicView =
                new SubTopicView(
                        201L,
                        "서브 토픽 뷰 제목",
                        "서브 토픽 뷰 내용",
                        ImportanceLevel.OPTIONAL,
                        null,
                        false,
                        false,
                        List.of(resourceSubTopicView),
                        now,
                        now);

        ResourceTopicView resourceTopicView =
                new ResourceTopicView(
                        301L,
                        "토픽 리소스 뷰 이름",
                        ResourceType.ROADMAP,
                        1,
                        "https://example.com/topic-resource-view");

        TopicView topicView =
                new TopicView(
                        401L,
                        List.of(resourceTopicView),
                        List.of(subTopicView),
                        "토픽 뷰 제목",
                        "토픽 뷰 내용",
                        ImportanceLevel.RECOMMENDED,
                        1,
                        now,
                        now,
                        null,
                        false,
                        false);

        return new RoadMapView(
                roadMapId,
                "스프링 완전 정복",
                "스프링 부트와 JPA를 이용한 백엔드 개발",
                null,
                now,
                now,
                false,
                false,
                categoryId,
                List.of(topicView));
    }

    private RestDocumentationResultHandler documentCreateRoadMap() {
        return document(
                "roadmap-create",
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("로드맵 제목"),
                        fieldWithPath("description")
                                .type(JsonFieldType.STRING)
                                .description("로드맵 설명"),
                        fieldWithPath("isDraft")
                                .type(JsonFieldType.BOOLEAN)
                                .description("임시 저장 여부"),
                        fieldWithPath("categoryId")
                                .type(JsonFieldType.NUMBER)
                                .description("카테고리 ID"),
                        fieldWithPath("topics").type(JsonFieldType.ARRAY).description("토픽 목록"),
                        fieldWithPath("topics[].title")
                                .type(JsonFieldType.STRING)
                                .description("토픽 제목"),
                        fieldWithPath("topics[].content")
                                .type(JsonFieldType.STRING)
                                .description("토픽 설명"),
                        fieldWithPath("topics[].importanceLevel")
                                .type(JsonFieldType.STRING)
                                .description("중요도"),
                        fieldWithPath("topics[].order")
                                .type(JsonFieldType.NUMBER)
                                .description("순서"),
                        fieldWithPath("topics[].isDraft")
                                .type(JsonFieldType.BOOLEAN)
                                .description("토픽 임시 저장 여부"),
                        fieldWithPath("topics[].resources")
                                .type(JsonFieldType.ARRAY)
                                .description("토픽 리소스 목록"),
                        fieldWithPath("topics[].resources[].name")
                                .type(JsonFieldType.STRING)
                                .description("토픽 리소스 이름"),
                        fieldWithPath("topics[].resources[].resourceType")
                                .type(JsonFieldType.STRING)
                                .description("토픽 리소스 타입"),
                        fieldWithPath("topics[].resources[].order")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 리소스 순서"),
                        fieldWithPath("topics[].resources[].link")
                                .type(JsonFieldType.STRING)
                                .description("토픽 리소스 링크"),
                        fieldWithPath("topics[].subTopics")
                                .type(JsonFieldType.ARRAY)
                                .description("하위 토픽 목록"),
                        fieldWithPath("topics[].subTopics[].title")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 제목"),
                        fieldWithPath("topics[].subTopics[].content")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 내용"),
                        fieldWithPath("topics[].subTopics[].importanceLevel")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 중요도"),
                        fieldWithPath("topics[].subTopics[].isDraft")
                                .type(JsonFieldType.BOOLEAN)
                                .description("서브 토픽 임시 저장 여부"),
                        fieldWithPath("topics[].subTopics[].resources")
                                .type(JsonFieldType.ARRAY)
                                .description("서브 토픽 리소스 목록"),
                        fieldWithPath("topics[].subTopics[].resources[].name")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 리소스 이름"),
                        fieldWithPath("topics[].subTopics[].resources[].order")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 리소스 순서"),
                        fieldWithPath("topics[].subTopics[].resources[].resourceType")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 리소스 타입"),
                        fieldWithPath("topics[].subTopics[].resources[].link")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 리소스 링크")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id")
                                .type(JsonFieldType.NUMBER)
                                .description("생성된 로드맵 ID")));
    }

    private RestDocumentationResultHandler documentCloneRoadMap() {
        return document(
                "roadmap-clone",
                pathParameters(parameterWithName("id").description("원본 로드맵 ID")),
                requestFields(
                        fieldWithPath("userId")
                                .type(JsonFieldType.NUMBER)
                                .description("복제할 사용자 ID"),
                        fieldWithPath("categoryId")
                                .type(JsonFieldType.NUMBER)
                                .description("복제할 카테고리 ID")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id")
                                .type(JsonFieldType.NUMBER)
                                .description("복제된 로드맵 ID")));
    }

    private RestDocumentationResultHandler documentGetRoadMapById() {
        return document(
                "roadmap-get-by-id",
                pathParameters(parameterWithName("id").description("로드맵 ID")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("로드맵 ID"),
                        fieldWithPath("data.title")
                                .type(JsonFieldType.STRING)
                                .description("로드맵 제목"),
                        fieldWithPath("data.description")
                                .type(JsonFieldType.STRING)
                                .description("로드맵 설명"),
                        fieldWithPath("data.deletedAt")
                                .type(JsonFieldType.NULL)
                                .description("삭제 일시"),
                        fieldWithPath("data.createdAt")
                                .type(JsonFieldType.STRING)
                                .description("생성 일시"),
                        fieldWithPath("data.modifiedAt")
                                .type(JsonFieldType.STRING)
                                .description("수정 일시"),
                        fieldWithPath("data.isDeleted")
                                .type(JsonFieldType.BOOLEAN)
                                .description("삭제 여부"),
                        fieldWithPath("data.isDraft")
                                .type(JsonFieldType.BOOLEAN)
                                .description("임시 저장 여부"),
                        fieldWithPath("data.categoryId")
                                .type(JsonFieldType.NUMBER)
                                .description("카테고리 ID"),
                        fieldWithPath("data.topics")
                                .type(JsonFieldType.ARRAY)
                                .description("로드맵 토픽 목록"),
                        fieldWithPath("data.topics[].id")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 ID"),
                        fieldWithPath("data.topics[].title")
                                .type(JsonFieldType.STRING)
                                .description("토픽 제목"),
                        fieldWithPath("data.topics[].content")
                                .type(JsonFieldType.STRING)
                                .description("토픽 내용"),
                        fieldWithPath("data.topics[].importanceLevel")
                                .type(JsonFieldType.STRING)
                                .description("토픽 중요도"),
                        fieldWithPath("data.topics[].order")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 순서"),
                        fieldWithPath("data.topics[].createdAt")
                                .type(JsonFieldType.STRING)
                                .description("토픽 생성 일시"),
                        fieldWithPath("data.topics[].modifiedAt")
                                .type(JsonFieldType.STRING)
                                .description("토픽 수정 일시"),
                        fieldWithPath("data.topics[].deletedAt")
                                .type(JsonFieldType.NULL)
                                .description("토픽 삭제 일시"),
                        fieldWithPath("data.topics[].isDraft")
                                .type(JsonFieldType.BOOLEAN)
                                .description("토픽 임시 저장 여부"),
                        fieldWithPath("data.topics[].isDeleted")
                                .type(JsonFieldType.BOOLEAN)
                                .description("토픽 삭제 여부"),
                        fieldWithPath("data.topics[].resources")
                                .type(JsonFieldType.ARRAY)
                                .description("토픽 리소스 목록"),
                        fieldWithPath("data.topics[].resources[].id")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 리소스 ID"),
                        fieldWithPath("data.topics[].resources[].name")
                                .type(JsonFieldType.STRING)
                                .description("토픽 리소스 이름"),
                        fieldWithPath("data.topics[].resources[].resourceType")
                                .type(JsonFieldType.STRING)
                                .description("토픽 리소스 타입"),
                        fieldWithPath("data.topics[].resources[].order")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 리소스 순서"),
                        fieldWithPath("data.topics[].resources[].link")
                                .type(JsonFieldType.STRING)
                                .description("토픽 리소스 링크"),
                        fieldWithPath("data.topics[].subTopics")
                                .type(JsonFieldType.ARRAY)
                                .description("하위 토픽 목록"),
                        fieldWithPath("data.topics[].subTopics[].id")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 ID"),
                        fieldWithPath("data.topics[].subTopics[].title")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 제목"),
                        fieldWithPath("data.topics[].subTopics[].content")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 내용"),
                        fieldWithPath("data.topics[].subTopics[].importanceLevel")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 중요도"),
                        fieldWithPath("data.topics[].subTopics[].deletedAt")
                                .type(JsonFieldType.NULL)
                                .description("서브 토픽 삭제 일시"),
                        fieldWithPath("data.topics[].subTopics[].isDraft")
                                .type(JsonFieldType.BOOLEAN)
                                .description("서브 토픽 임시 저장 여부"),
                        fieldWithPath("data.topics[].subTopics[].isDeleted")
                                .type(JsonFieldType.BOOLEAN)
                                .description("서브 토픽 삭제 여부"),
                        fieldWithPath("data.topics[].subTopics[].createdAt")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 생성 일시"),
                        fieldWithPath("data.topics[].subTopics[].modifiedAt")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 수정 일시"),
                        fieldWithPath("data.topics[].subTopics[].resources")
                                .type(JsonFieldType.ARRAY)
                                .description("서브 토픽 리소스 목록"),
                        fieldWithPath("data.topics[].subTopics[].resources[].id")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 리소스 ID"),
                        fieldWithPath("data.topics[].subTopics[].resources[].name")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 리소스 이름"),
                        fieldWithPath("data.topics[].subTopics[].resources[].resourceType")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 리소스 타입"),
                        fieldWithPath("data.topics[].subTopics[].resources[].order")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 리소스 순서"),
                        fieldWithPath("data.topics[].subTopics[].resources[].link")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 리소스 링크")));
    }
}
