package com.kllhy.roadmap.user.service.view;

import com.kllhy.roadmap.user.domain.User;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import java.time.Instant;

public record UserView(Long id, String email, AccountStatus status, Instant leftAt) {
    public static UserView of(User user) {

        return new UserView(user.getId(), user.getEmail(), user.getStatus(), user.getLeftAt());
    }
}
