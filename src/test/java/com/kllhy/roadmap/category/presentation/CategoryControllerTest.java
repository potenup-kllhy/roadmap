package com.kllhy.roadmap.category.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kllhy.roadmap.category.application.query.CategoryQueryService;
import com.kllhy.roadmap.category.application.query.dto.CategoryView;
import com.kllhy.roadmap.common.RestDocsSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class CategoryControllerTest extends RestDocsSupport {

    @MockitoBean private CategoryQueryService categoryQueryService;

    @DisplayName("전체 카테고리 목록을 조회한다.")
    @Test
    void getCategories() throws Exception {
        // given
        List<CategoryView> response =
                List.of(new CategoryView(1L, "SKILL", "자바"), new CategoryView(2L, "ROLE", "백엔드"));
        given(categoryQueryService.getAllCategoriesOrdered()).willReturn(response);

        // when & then
        mockMvc.perform(get("/api-v1/categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentGetCategories());
    }

    private RestDocumentationResultHandler documentGetCategories() {
        return document(
                "category-get-all",
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("[].type")
                                .type(JsonFieldType.STRING)
                                .description("카테고리 타입")));
    }
}
