//package com.kllhy.roadmap.roadmap.domain.model;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.kllhy.roadmap.roadmap.domain.model.creation_spec.*;
//import com.kllhy.roadmap.roadmap.domain.model.enums.ImportanceLevel;
//import com.kllhy.roadmap.roadmap.domain.model.enums.ResourceType;
//import com.kllhy.roadmap.roadmap.domain.model.update_spec.*;
//import java.lang.reflect.Field;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class RoadMapUpdateTest {
//
//    @Test
//    @DisplayName("로드맵 업데이트 시 기존 토픽과 리소스를 비교하여 동기화한다")
//    void updateRoadMapSynchronizesAggregate() throws Exception {
//        RoadMap roadMap = createInitialRoadMap();
//
//        Topic existingTopic = roadMap.getTopics().get(0);
//        setId(existingTopic, 100L);
//        ResourceTopic existingResource = existingTopic.getResources().get(0);
//        setId(existingResource, 200L);
//        SubTopic existingSubTopic = existingTopic.getSubTopics().get(0);
//        setId(existingSubTopic, 300L);
//        ResourceSubTopic existingSubResource = existingSubTopic.getResources().get(0);
//        setId(existingSubResource, 400L);
//
//        UpdateRoadMap updateSpec =
//                new UpdateRoadMap(
//
//                        "업데이트된 로드맵",
//                        "새로운 설명",
//                        true,
//                        2L,
//                        List.of(
//                                new UpdateTopic(
//                                        100L,
//                                        "토픽 1-수정",
//                                        "내용 수정",
//                                        ImportanceLevel.RECOMMENDED,
//                                        1,
//                                        true,
//                                        List.of(
//                                                new UpdateResourceTopic(
//                                                        200L,
//                                                        "자료 수정",
//                                                        1,
//                                                        ResourceType.POST,
//                                                        "https://resource.example"),
//                                                new UpdateResourceTopic(
//                                                        null,
//                                                        "새 자료",
//                                                        2,
//                                                        ResourceType.VIDEO,
//                                                        "https://video.example")),
//                                        List.of(
//                                                new UpdateSubTopic(
//                                                        300L,
//                                                        "서브토픽 수정",
//                                                        "서브 내용",
//                                                        ImportanceLevel.OPTIONAL,
//                                                        true,
//                                                        List.of(
//                                                                new UpdateResourceSubTopic(
//                                                                        400L,
//                                                                        "서브 리소스 수정",
//                                                                        1,
//                                                                        ResourceType.OFFICIAL,
//                                                                        "https://sub.example"))))),
//                                new UpdateTopic(
//                                        null,
//                                        "토픽 2",
//                                        "두 번째",
//                                        ImportanceLevel.DEFAULT,
//                                        2,
//                                        false,
//                                        List.of(),
//                                        List.of())));
//
//        roadMap.update(updateSpec);
//
//        assertThat(roadMap.getTitle()).isEqualTo("업데이트된 로드맵");
//        assertThat(roadMap.getCategoryId()).isEqualTo(2L);
//        assertThat(roadMap.getTopics()).hasSize(2);
//
//        Topic updatedTopic = roadMap.getTopics().get(0);
//        assertThat(updatedTopic.getId()).isEqualTo(100L);
//        assertThat(updatedTopic.getTitle()).isEqualTo("토픽 1-수정");
//        assertThat(updatedTopic.getResources()).hasSize(2);
//        assertThat(updatedTopic.getResources().get(0).getId()).isEqualTo(200L);
//        assertThat(updatedTopic.getResources().get(0).getName()).isEqualTo("자료 수정");
//        assertThat(updatedTopic.getResources().get(1).getId()).isNull();
//
//        SubTopic updatedSubTopic = updatedTopic.getSubTopics().get(0);
//        assertThat(updatedSubTopic.getId()).isEqualTo(300L);
//        assertThat(updatedSubTopic.getTitle()).isEqualTo("서브토픽 수정");
//        assertThat(updatedSubTopic.getResources()).hasSize(1);
//        assertThat(updatedSubTopic.getResources().get(0).getId()).isEqualTo(400L);
//        assertThat(updatedSubTopic.getResources().get(0).getName()).isEqualTo("서브 리소스 수정");
//
//        Topic newTopic = roadMap.getTopics().get(1);
//        assertThat(newTopic.getId()).isNull();
//        assertThat(newTopic.getTitle()).isEqualTo("토픽 2");
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 토픽 ID로 업데이트하면 예외가 발생한다")
//    void updateRoadMapWithUnknownTopicId() throws Exception {
//        RoadMap roadMap = createInitialRoadMap();
//        Topic existingTopic = roadMap.getTopics().get(0);
//        setId(existingTopic, 10L);
//
//        UpdateRoadMap updateSpec =
//                new UpdateRoadMap(
//                        "잘못된 로드맵",
//                        "desc",
//                        false,
//                        1L,
//                        List.of(
//                                new UpdateTopic(
//                                        99L,
//                                        "존재하지 않는",
//                                        null,
//                                        ImportanceLevel.DEFAULT,
//                                        1,
//                                        false,
//                                        List.of(),
//                                        List.of())));
//
//        assertThatThrownBy(() -> roadMap.update(updateSpec))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("존재하지 않는 Topic id");
//    }
//
//    private RoadMap createInitialRoadMap() {
//        CreationRoadMap creationSpec =
//                new CreationRoadMap(
//                        "로드맵",
//                        "설명",
//                        false,
//                        1L,
//                        1L,
//                        List.of(
//                                new CreationTopic(
//                                        "토픽 1",
//                                        "내용",
//                                        ImportanceLevel.RECOMMENDED,
//                                        1,
//                                        false,
//                                        List.of(
//                                                new CreationResourceTopic(
//                                                        "자료",
//                                                        ResourceType.POST,
//                                                        1,
//                                                        "https://example.com")),
//                                        List.of(
//                                                new CreationSubTopic(
//                                                        "서브토픽",
//                                                        "서브 내용",
//                                                        ImportanceLevel.RECOMMENDED,
//                                                        false,
//                                                        List.of(
//                                                                new CreationResourceSubTopic(
//                                                                        "서브 리소스",
//                                                                        1,
//                                                                        ResourceType.POST,
//                                                                        "https://sub.example.com")))))));
//
//        return RoadMap.create(creationSpec);
//    }
//
//    private void setId(Object target, long id) throws Exception {
//        Field field = findIdField(target.getClass());
//        field.setAccessible(true);
//        field.set(target, id);
//    }
//
//    private Field findIdField(Class<?> type) throws NoSuchFieldException {
//        Class<?> current = type;
//        while (current != null) {
//            try {
//                return current.getDeclaredField("id");
//            } catch (NoSuchFieldException ignored) {
//                current = current.getSuperclass();
//            }
//        }
//        throw new NoSuchFieldException("id");
//    }
//}
