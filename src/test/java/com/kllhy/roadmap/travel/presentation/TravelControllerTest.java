package com.kllhy.roadmap.travel.presentation;

import static com.kllhy.roadmap.common.response.SuccessCode.CREATED;
import static com.kllhy.roadmap.common.response.SuccessCode.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kllhy.roadmap.common.RestDocsSupport;
import com.kllhy.roadmap.travel.application.service.command.TravelService;
import com.kllhy.roadmap.travel.application.service.query.TravelQueryService;
import com.kllhy.roadmap.travel.application.view.ProgressSubTopicView;
import com.kllhy.roadmap.travel.application.view.ProgressTopicView;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.domain.model.enums.TravelProgressStatus;
import com.kllhy.roadmap.travel.presentation.request.ProgressSubTopicUpdateRequest;
import com.kllhy.roadmap.travel.presentation.request.ProgressTopicUpdateRequest;
import com.kllhy.roadmap.travel.presentation.request.TravelCreateRequest;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class TravelControllerTest extends RestDocsSupport {

    @MockitoBean private TravelService travelService;

    @MockitoBean private TravelQueryService travelQueryService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("여행 생성 API")
    void createTravel() throws Exception {
        // given
        TravelCreateRequest request = new TravelCreateRequest(1L, 100L);
        Long createdTravelId = 1L;
        when(travelService.create(anyLong(), anyLong())).thenReturn(createdTravelId);

        // when // then
        mockMvc.perform(
                        post("/api-v1/travels")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(CREATED.getCode()))
                .andExpect(jsonPath("$.message").value(CREATED.getMessage()))
                .andExpect(jsonPath("$.data").value(createdTravelId))
                .andDo(documentCreateTravel());
    }

    @Test
    @DisplayName("여행 단건 조회 API")
    void getTravelById() throws Exception {
        // given
        Long travelId = 1L;
        TravelView travelView = createSampleTravelView();
        when(travelQueryService.getById(anyLong())).thenReturn(travelView);

        // when // then
        mockMvc.perform(get("/api-v1/travels/{id}", travelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.userId").value(travelView.userId()))
                .andDo(documentGetTravelById()); // ⬅️ 문서화 분리
    }

    @Test
    @DisplayName("여행 업데이트 API")
    void updateTravel() throws Exception {
        // given
        TravelUpdateRequest request = createSampleTravelUpdateRequest();
        TravelView updatedTravelView = createSampleUpdatedTravelView();
        when(travelService.update(any(TravelUpdateRequest.class))).thenReturn(updatedTravelView);

        // when // then
        mockMvc.perform(
                        put("/api-v1/travels")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.status").value(updatedTravelView.status().name()))
                .andDo(documentUpdateTravel());
    }

    @Test
    @DisplayName("유저 ID로 여행 목록 조회 API")
    void getTravelsByUserId() throws Exception {
        // given
        Long userId = 1L;
        List<TravelView> travelViews = createSampleTravelViewList();
        when(travelQueryService.getByUser(anyLong())).thenReturn(travelViews);

        // when // then
        mockMvc.perform(get("/api-v1/travels/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("$.data[0].userId").value(travelViews.get(0).userId()))
                .andDo(documentGetTravelsByUserId()); // ⬅️ 문서화 분리
    }

    private TravelView createSampleTravelView() {
        return new TravelView(
                1L,
                100L,
                TravelProgressStatus.IN_PROGRESS,
                List.of(
                        new ProgressTopicView(
                                1L,
                                10L,
                                ProgressStatus.IN_PROGRESS,
                                List.of(new ProgressSubTopicView(1L, 101L, ProgressStatus.TODO)))));
    }

    private TravelUpdateRequest createSampleTravelUpdateRequest() {
        return new TravelUpdateRequest(
                1L,
                1L,
                List.of(new ProgressTopicUpdateRequest(1L, ProgressStatus.DONE)),
                List.of(new ProgressSubTopicUpdateRequest(1L, 1L, ProgressStatus.DONE)));
    }

    private TravelView createSampleUpdatedTravelView() {
        return new TravelView(
                1L,
                100L,
                TravelProgressStatus.DONE,
                List.of(
                        new ProgressTopicView(
                                1L,
                                10L,
                                ProgressStatus.DONE,
                                List.of(new ProgressSubTopicView(1L, 101L, ProgressStatus.DONE)))));
    }

    private List<TravelView> createSampleTravelViewList() {
        TravelView view1 =
                new TravelView(
                        1L,
                        100L,
                        TravelProgressStatus.IN_PROGRESS,
                        List.of(
                                new ProgressTopicView(
                                        1L,
                                        10L,
                                        ProgressStatus.IN_PROGRESS,
                                        List.of(
                                                new ProgressSubTopicView(
                                                        1L, 101L, ProgressStatus.TODO)))));

        TravelView view2 =
                new TravelView(
                        1L,
                        101L,
                        TravelProgressStatus.DONE,
                        List.of(
                                new ProgressTopicView(
                                        2L,
                                        20L,
                                        ProgressStatus.DONE,
                                        List.of(
                                                new ProgressSubTopicView(
                                                        2L, 201L, ProgressStatus.DONE)))));

        return List.of(view1, view2);
    }

    private RestDocumentationResultHandler documentCreateTravel() {
        return document(
                "travel-create",
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                        fieldWithPath("roadmapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 여행 ID")));
    }

    private RestDocumentationResultHandler documentGetTravelById() {
        return document(
                "travel-get-by-id",
                pathParameters(parameterWithName("id").description("조회할 여행 ID")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.userId")
                                .type(JsonFieldType.NUMBER)
                                .description("유저 ID"),
                        fieldWithPath("data.roadmapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID"),
                        fieldWithPath("data.status")
                                .type(JsonFieldType.STRING)
                                .description("여행 진행 상태"),
                        fieldWithPath("data.progressTopics[]")
                                .type(JsonFieldType.ARRAY)
                                .description("진행 중인 토픽 목록"),
                        fieldWithPath("data.progressTopics[].progressTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 토픽 ID"),
                        fieldWithPath("data.progressTopics[].topicId")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 ID"),
                        fieldWithPath("data.progressTopics[].status")
                                .type(JsonFieldType.STRING)
                                .description("토픽 진행 상태"),
                        fieldWithPath("data.progressTopics[].progressSubTopics[]")
                                .type(JsonFieldType.ARRAY)
                                .description("진행 중인 서브 토픽 목록"),
                        fieldWithPath(
                                        "data.progressTopics[].progressSubTopics[].progressSubTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 서브 토픽 ID"),
                        fieldWithPath("data.progressTopics[].progressSubTopics[].subTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 ID"),
                        fieldWithPath("data.progressTopics[].progressSubTopics[].status")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 진행 상태")));
    }

    private RestDocumentationResultHandler documentUpdateTravel() {
        return document(
                "travel-update",
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                        fieldWithPath("travelId").type(JsonFieldType.NUMBER).description("여행 ID"),
                        fieldWithPath("progressTopicUpdates[]")
                                .type(JsonFieldType.ARRAY)
                                .optional()
                                .description("업데이트할 토픽 진행 상태 목록"),
                        fieldWithPath("progressTopicUpdates[].progressTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 토픽 ID"),
                        fieldWithPath("progressTopicUpdates[].status")
                                .type(JsonFieldType.STRING)
                                .description("업데이트할 토픽 진행 상태"),
                        fieldWithPath("progressSubTopicUpdateRequests[]")
                                .type(JsonFieldType.ARRAY)
                                .optional()
                                .description("업데이트할 서브 토픽 진행 상태 목록"),
                        fieldWithPath("progressSubTopicUpdateRequests[].progressTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 토픽 ID"),
                        fieldWithPath("progressSubTopicUpdateRequests[].progressSubTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 서브 토픽 ID"),
                        fieldWithPath("progressSubTopicUpdateRequests[].status")
                                .type(JsonFieldType.STRING)
                                .description("업데이트할 서브 토픽 진행 상태")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.userId")
                                .type(JsonFieldType.NUMBER)
                                .description("유저 ID"),
                        fieldWithPath("data.roadmapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID"),
                        fieldWithPath("data.status")
                                .type(JsonFieldType.STRING)
                                .description("여행 진행 상태"),
                        fieldWithPath("data.progressTopics[]")
                                .type(JsonFieldType.ARRAY)
                                .description("진행 중인 토픽 목록"),
                        fieldWithPath("data.progressTopics[].progressTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 토픽 ID"),
                        fieldWithPath("data.progressTopics[].topicId")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 ID"),
                        fieldWithPath("data.progressTopics[].status")
                                .type(JsonFieldType.STRING)
                                .description("토픽 진행 상태"),
                        fieldWithPath("data.progressTopics[].progressSubTopics[]")
                                .type(JsonFieldType.ARRAY)
                                .description("진행 중인 서브 토픽 목록"),
                        fieldWithPath(
                                        "data.progressTopics[].progressSubTopics[].progressSubTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 서브 토픽 ID"),
                        fieldWithPath("data.progressTopics[].progressSubTopics[].subTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 ID"),
                        fieldWithPath("data.progressTopics[].progressSubTopics[].status")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 진행 상태")));
    }

    private RestDocumentationResultHandler documentGetTravelsByUserId() {
        return document(
                "travel-get-by-user-id",
                pathParameters(parameterWithName("userId").description("조회할 유저 ID")),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("여행 목록"),
                        fieldWithPath("data[].userId")
                                .type(JsonFieldType.NUMBER)
                                .description("유저 ID"),
                        fieldWithPath("data[].roadmapId")
                                .type(JsonFieldType.NUMBER)
                                .description("로드맵 ID"),
                        fieldWithPath("data[].status")
                                .type(JsonFieldType.STRING)
                                .description("여행 진행 상태"),
                        fieldWithPath("data[].progressTopics[]")
                                .type(JsonFieldType.ARRAY)
                                .description("진행 중인 토픽 목록"),
                        fieldWithPath("data[].progressTopics[].progressTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 토픽 ID"),
                        fieldWithPath("data[].progressTopics[].topicId")
                                .type(JsonFieldType.NUMBER)
                                .description("토픽 ID"),
                        fieldWithPath("data[].progressTopics[].status")
                                .type(JsonFieldType.STRING)
                                .description("토픽 진행 상태"),
                        fieldWithPath("data[].progressTopics[].progressSubTopics[]")
                                .type(JsonFieldType.ARRAY)
                                .description("진행 중인 서브 토픽 목록"),
                        fieldWithPath(
                                        "data[].progressTopics[].progressSubTopics[].progressSubTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("진행 중인 서브 토픽 ID"),
                        fieldWithPath("data[].progressTopics[].progressSubTopics[].subTopicId")
                                .type(JsonFieldType.NUMBER)
                                .description("서브 토픽 ID"),
                        fieldWithPath("data[].progressTopics[].progressSubTopics[].status")
                                .type(JsonFieldType.STRING)
                                .description("서브 토픽 진행 상태")));
    }
}
