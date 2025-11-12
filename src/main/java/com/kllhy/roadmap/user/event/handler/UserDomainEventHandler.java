package com.kllhy.roadmap.user.event.handler;

import com.kllhy.roadmap.user.event.UserAccountStatusUpdated;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserDomainEventHandler {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(UserDomainEventHandler.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(UserAccountStatusUpdated event) {
        log.info("hello!!!! userId={} status={}", event.userId(), event.newStatus());
    }
}
