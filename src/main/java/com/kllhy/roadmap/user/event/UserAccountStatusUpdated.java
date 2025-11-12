package com.kllhy.roadmap.user.event;

import com.kllhy.roadmap.common.event.BaseDomainEvent;
import java.time.Instant;

public final class UserAccountStatusUpdated extends BaseDomainEvent {
    private final Long userId;
    private final String oldStatus;
    private final String newStatus;
    private final Instant leftAt;

    public UserAccountStatusUpdated(
            Long userId, String oldStatus, String newStatus, Instant leftAt) {
        super();
        this.userId = userId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.leftAt = leftAt;
    }

    public Long userId() {
        return userId;
    }

    public String oldStatus() {
        return oldStatus;
    }

    public String newStatus() {
        return newStatus;
    }

    public Instant leftAt() {
        return leftAt;
    }
}
