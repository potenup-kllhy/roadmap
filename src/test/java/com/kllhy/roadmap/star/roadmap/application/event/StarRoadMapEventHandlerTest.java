package com.kllhy.roadmap.star.roadmap.application.event;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kllhy.roadmap.star.roadmap.application.command.StarRoadMapCommandService;
import com.kllhy.roadmap.user.event.UserAccountStatusUpdated;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class StarRoadMapEventHandlerTest {

    @Autowired private ApplicationEventPublisher eventPublisher;

    @MockitoBean private StarRoadMapCommandService starRoadmapCommandService;

    @Autowired private TransactionTemplate transactionTemplate;

    @Test
    @DisplayName("유저 상태가 BLOCKED로 변경되면, 해당 유저의 모든 별점을 삭제하는 서비스가 호출된다")
    void whenUserStatusIsBlocked_thenDeleteAllStarsIsCalled() {
        // given
        Long userId = 123L;
        UserAccountStatusUpdated event =
                new UserAccountStatusUpdated(userId, "ACTIVE", "BLOCKED", Instant.now());

        // when
        transactionTemplate.execute(
                status -> {
                    eventPublisher.publishEvent(event);
                    return null;
                });

        // then
        verify(starRoadmapCommandService, times(1)).deleteAllStarByUserId(userId);
    }

    @Test
    @DisplayName("유저 상태가 ACTIVE로 변경되면, 별점 삭제 서비스는 호출되지 않는다")
    void whenUserStatusIsActive_thenDeleteAllStarsIsNotCalled() {
        // given
        Long userId = 123L;
        UserAccountStatusUpdated event =
                new UserAccountStatusUpdated(userId, "BLOCKED", "ACTIVE", Instant.now());

        // when
        transactionTemplate.execute(
                status -> {
                    eventPublisher.publishEvent(event);
                    return null;
                });

        // then
        verify(starRoadmapCommandService, never()).deleteAllStarByUserId(anyLong());
    }
}
