package com.ohgiraffers.loadmapuser.domain.query.dto;

import com.ohgiraffers.loadmapuser.domain.AccountStatus;
import com.ohgiraffers.loadmapuser.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record UserQueryResult(
        Long id,
        String loginId,
        String email,
        AccountStatus accountStatus
) {
    public static UserQueryResult toQueryResult(User user){
        if (user == null) {
            log.warn("Attempted to convert null user to UserQueryResult");
            throw new IllegalArgumentException("User cannot be null");
        }

        return new UserQueryResult(
                user.getId(),
                user.getLoginId(),
                user.getEmail(),
                user.getAccountStatus()
        );
    }
}

