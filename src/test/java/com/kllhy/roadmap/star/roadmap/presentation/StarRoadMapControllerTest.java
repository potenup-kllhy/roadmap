package com.kllhy.roadmap.star.roadmap.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kllhy.roadmap.common.RestDocsSupport;
import com.kllhy.roadmap.star.roadmap.application.command.StarRoadMapCommandService;
import com.kllhy.roadmap.star.roadmap.application.query.StarRoadMapQueryService;
import com.kllhy.roadmap.star.roadmap.application.query.dto.StarRoadMapView;
import com.kllhy.roadmap.star.roadmap.presentation.request.CreateStarRoadMapRequest;
import com.kllhy.roadmap.star.roadmap.presentation.request.DeleteStarRoadMapRequest;
import com.kllhy.roadmap.star.roadmap.presentation.request.UpdateStarRoadMapRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class StarRoadMapControllerTest extends RestDocsSupport {

    @MockitoBean private StarRoadMapCommandService starRoadMapCommandService;

    @MockitoBean private StarRoadMapQueryService starRoadMapQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("별점을 생성한다.")
    @Test
    void createStarRoadMap() throws Exception {
        // given
        CreateStarRoadMapRequest request = new CreateStarRoadMapRequest(1L, 1L, 5);
        given(
                        starRoadMapCommandService.create(
                                request.userId(), request.roadmapId(), request.value()))
                .willReturn(1L);

        // when & then
        mockMvc.perform(
                        post("/api-v1/star-roadmaps")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(documentCreateStarRoadMap());
    }

    @DisplayName("ID로 별점을 조회한다.")
    @Test
    void getStarRoadMapById() throws Exception {
        // given
        long starRoadMapId = 1L;
        StarRoadMapView response = new StarRoadMapView(1L, 1L, 5);
        given(starRoadMapQueryService.getById(starRoadMapId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api-v1/star-roadmaps/{id}", starRoadMapId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentGetStarRoadMapById());
    }

    @DisplayName("사용자 ID로 별점 목록을 조회한다.")
    @Test
    void getStarRoadMapsByUserId() throws Exception {
        // given
        long userId = 1L;
        List<StarRoadMapView> response =
                List.of(new StarRoadMapView(userId, 1L, 5), new StarRoadMapView(userId, 2L, 4));
        given(starRoadMapQueryService.getAllStarByUserId(userId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api-v1/star-roadmaps").param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentGetStarRoadMapsByUserId());
    }

    @DisplayName("로드맵 ID로 별점 목록을 조회한다.")
    @Test
    void getStarRoadMapsByRoadmapId() throws Exception {
        // given
        long roadmapId = 1L;
        List<StarRoadMapView> response =
                List.of(
                        new StarRoadMapView(1L, roadmapId, 5),
                        new StarRoadMapView(2L, roadmapId, 4));
        given(starRoadMapQueryService.getAllStarByRoadMapId(roadmapId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api-v1/star-roadmaps").param("roadmapId", String.valueOf(roadmapId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentGetStarRoadMapsByRoadmapId());
    }

    @DisplayName("별점을 수정한다.")
    @Test
    void updateStarRoadMap() throws Exception {
        // given
        long starRoadMapId = 1L;
        UpdateStarRoadMapRequest request = new UpdateStarRoadMapRequest(1L, 4);

        // when & then
        mockMvc.perform(
                        put("/api-v1/star-roadmaps/{id}", starRoadMapId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentUpdateStarRoadMap());
    }

    @DisplayName("사용자와 로드맵 ID로 별점을 삭제한다.")
    @Test
    void deleteStarRoadMapByUserAndRoadmap() throws Exception {
        // given
        DeleteStarRoadMapRequest request = new DeleteStarRoadMapRequest(1L, 1L);

        // when & then
        mockMvc.perform(
                        delete("/api-v1/star-roadmaps")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(documentDeleteStarRoadMap());
    }

    private RestDocumentationResultHandler documentCreateStarRoadMap() {
        return document(
                "star-roadmap-create",
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("roadmapId").type(JsonFieldType.NUMBER).description("로드맵 ID"),
                        fieldWithPath("value").type(JsonFieldType.NUMBER).description("별점 (0-5)")));
    }

    private RestDocumentationResultHandler documentGetStarRoadMapById() {
        return document(
                "star-roadmap-get-by-id",
                pathParameters(parameterWithName("id").description("별점 ID")),
                responseFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("roadMapId").type(JsonFieldType.NUMBER).description("로드맵 ID"),
                        fieldWithPath("value").type(JsonFieldType.NUMBER).description("별점")));
    }

    private RestDocumentationResultHandler documentGetStarRoadMapsByUserId() {
        return document(
                "star-roadmap-get-by-user-id",
                queryParameters(parameterWithName("userId").description("사용자 ID")),
                responseFields(
                        fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("[].roadMapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID"),
                        fieldWithPath("[].value").type(JsonFieldType.NUMBER).description("별점")));
    }

    private RestDocumentationResultHandler documentGetStarRoadMapsByRoadmapId() {
        return document(
                "star-roadmap-get-by-roadmap-id",
                queryParameters(parameterWithName("roadmapId").description("로드맵 ID")),
                responseFields(
                        fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("[].roadMapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID"),
                        fieldWithPath("[].value").type(JsonFieldType.NUMBER).description("별점")));
    }

    private RestDocumentationResultHandler documentUpdateStarRoadMap() {
        return document(
                "star-roadmap-update",
                pathParameters(parameterWithName("id").description("별점 ID")),
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("value")
                                .type(JsonFieldType.NUMBER)
                                .description("새로운 별점 (0-5)")));
    }

    private RestDocumentationResultHandler documentDeleteStarRoadMap() {
        return document(
                "star-roadmap-delete",
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                        fieldWithPath("roadmapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID")));
    }
}
