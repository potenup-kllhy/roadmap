package com.kllhy.roadmap.star.roadmap.application.event;

import com.kllhy.roadmap.star.roadmap.application.command.StarRoadMapCommandService;
import com.kllhy.roadmap.user.event.UserAccountStatusUpdated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class StarRoadMapEventHandler {

    private final StarRoadMapCommandService starRoadMapCommandService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlerUserAccountStatusUpdated(UserAccountStatusUpdated event) {
        String newStatus = event.newStatus();
        Long userId = event.userId();

        if("BLOCKED".equals(newStatus)) {
            log.info("User account status updated to BLOCKED. Deleting star roadmaps for userId: {}", userId);
            starRoadMapCommandService.deleteAllStarByUserId(userId);
        }
    }
}
