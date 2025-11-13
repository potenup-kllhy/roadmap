package com.kllhy.roadmap.travel.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.travel.domain.model.ProgressTopic;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.travel.domain.model.enums.ProgressStatus;
import com.kllhy.roadmap.travel.presentation.request.ProgressSubTopicUpdateRequest;
import com.kllhy.roadmap.travel.presentation.request.ProgressTopicUpdateRequest;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.view.UserView;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TravelUpdateServiceTest {
    private final TravelUpdateService service = new TravelUpdateService();

    private static Travel travel(long userId, long rmId, Map<Long, List<Long>> structure) {
        var topicCmds =
                structure.entrySet().stream()
                        .map(
                                e ->
                                        new ProgressTopicCommand(
                                                e.getKey(),
                                                e.getValue() == null
                                                        ? List.of()
                                                        : e.getValue().stream()
                                                                .map(ProgressSubTopicCommand::new)
                                                                .toList()))
                        .toList();
        return Travel.create(new TravelCommand(userId, rmId, topicCmds));
    }

    private static Set<Long> topicIdsOf(Travel t) {
        return t.getTopics().stream().map(ProgressTopic::getTopicId).collect(Collectors.toSet());
    }

    private static ProgressStatus statusOfTopic(Travel t, long topicId) {
        return t.getTopicOrThrow(topicId).getStatus();
    }

    private static ProgressStatus statusOfSub(Travel t, long topicId, long subId) {
        return t.getTopicOrThrow(topicId).getSubTopics().stream()
                .filter(s -> s.getSubTopicId().equals(subId))
                .findFirst()
                .orElseThrow()
                .getStatus();
    }

    @Test
    @DisplayName("update: Topic/ SubTopic 동시 업데이트 성공")
    void update_success_both() {
        // given
        var user = new UserView(1L, null, AccountStatus.ACTIVE, null);
        var tr =
                travel(
                        1L,
                        100L,
                        Map.of(
                                10L, List.of(101L, 102L),
                                20L, List.of(201L)));

        var req =
                new TravelUpdateRequest(
                        user.id(),
                        999L,
                        List.of(new ProgressTopicUpdateRequest(20L, ProgressStatus.DONE)),
                        List.of(
                                new ProgressSubTopicUpdateRequest(
                                        10L, 102L, ProgressStatus.IN_PROGRESS)));

        // when
        var updated = service.update(user, tr, req);

        // then
        assertThat(topicIdsOf(updated)).containsExactlyInAnyOrder(10L, 20L);
        assertThat(statusOfTopic(updated, 20L)).isEqualTo(ProgressStatus.DONE);
        assertThat(statusOfSub(updated, 10L, 102L)).isEqualTo(ProgressStatus.IN_PROGRESS);
        // 다른 것들은 그대로
        assertThat(statusOfTopic(updated, 10L)).isEqualTo(ProgressStatus.TODO);
        assertThat(statusOfSub(updated, 10L, 101L)).isEqualTo(ProgressStatus.TODO);
        assertThat(statusOfSub(updated, 20L, 201L)).isEqualTo(ProgressStatus.TODO);
    }

    @Test
    @DisplayName("update: Topic만 업데이트(서브 null)")
    void update_topic_only() {
        var user = new UserView(1L, null, AccountStatus.ACTIVE, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));

        var req =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        List.of(new ProgressTopicUpdateRequest(10L, ProgressStatus.DONE)),
                        null);

        var updated = service.update(user, tr, req);

        assertThat(statusOfTopic(updated, 10L)).isEqualTo(ProgressStatus.DONE);
        assertThat(statusOfSub(updated, 10L, 101L)).isEqualTo(ProgressStatus.TODO);
    }

    @Test
    @DisplayName("update: SubTopic만 업데이트(토픽 null)")
    void update_sub_only() {
        var user = new UserView(1L, null, AccountStatus.ACTIVE, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));

        var req =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        null,
                        List.of(new ProgressSubTopicUpdateRequest(10L, 101L, ProgressStatus.DONE)));

        var updated = service.update(user, tr, req);
        assertThat(statusOfTopic(updated, 10L)).isEqualTo(ProgressStatus.TODO);
        assertThat(statusOfSub(updated, 10L, 101L)).isEqualTo(ProgressStatus.DONE);
    }

    @Test
    @DisplayName("update: 유저가 비활성 상태면 실패")
    void update_fail_inactive_user() {
        var user = new UserView(1L, null, AccountStatus.DISABLED, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));

        var req =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        List.of(new ProgressTopicUpdateRequest(10L, ProgressStatus.DONE)),
                        null);

        assertThatThrownBy(() -> service.update(user, tr, req)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("update: 본인 Travel이 아니면 실패")
    void update_fail_not_owner() {
        var user = new UserView(2L, null, AccountStatus.ACTIVE, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));

        var req =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        List.of(new ProgressTopicUpdateRequest(10L, ProgressStatus.DONE)),
                        null);

        assertThatThrownBy(() -> service.update(user, tr, req)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("update: 존재하지 않는 토픽/서브토픽이면 실패")
    void update_fail_not_found_ids() {
        var user = new UserView(1L, null, AccountStatus.ACTIVE, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));

        // 잘못된 topicId
        var badTopicReq =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        List.of(new ProgressTopicUpdateRequest(999L, ProgressStatus.DONE)),
                        null);
        assertThatThrownBy(() -> service.update(user, tr, badTopicReq))
                .isInstanceOf(DomainException.class);

        // 존재하지 않는 subTopicId
        var badSubReq =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        null,
                        List.of(new ProgressSubTopicUpdateRequest(10L, 999L, ProgressStatus.DONE)));
        assertThatThrownBy(() -> service.update(user, tr, badSubReq))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("update: Travel이 archiving 상태면 변경 불가")
    void update_fail_archived_travel() {
        var user = new UserView(1L, null, AccountStatus.ACTIVE, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));
        // root에서 비활성 처리
        tr.activate(ActiveStatus.INACTIVE);

        var req =
                new TravelUpdateRequest(
                        user.id(),
                        1L,
                        List.of(new ProgressTopicUpdateRequest(10L, ProgressStatus.DONE)),
                        null);

        assertThatThrownBy(() -> service.update(user, tr, req)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("update: 빈 리스트(null 아님)도 안전하게 처리")
    void update_empty_lists() {
        var user = new UserView(1L, null, AccountStatus.ACTIVE, null);
        var tr = travel(1L, 100L, Map.of(10L, List.of(101L)));

        var req = new TravelUpdateRequest(user.id(), 1L, List.of(), List.of());
        var updated = service.update(user, tr, req);

        // 아무 변화 없음
        assertThat(statusOfTopic(updated, 10L)).isEqualTo(ProgressStatus.TODO);
        assertThat(statusOfSub(updated, 10L, 101L)).isEqualTo(ProgressStatus.TODO);
    }
}
