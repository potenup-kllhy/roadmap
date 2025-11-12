package com.kllhy.roadmap.user.domain;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.event.UserAccountStatusUpdated;
import com.kllhy.roadmap.user.exception.UserErrorCode;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends AggregateRoot {

    @Getter
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, name = "password_hashed")
    private String password;

    @Getter
    @Column(nullable = false, name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Getter private Instant leftAt = null;

    private User(String email, String password) {
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.password = Objects.requireNonNull(password, "password must not be null");
    }

    public static User create(String email, String password) {
        return new User(email, password);
    }

    public void updateStatus(AccountStatus newStatus) {
        AccountStatus oldStatus = this.status;
        if (oldStatus == AccountStatus.DISABLED && newStatus == AccountStatus.ACTIVE) {
            throw new DomainException(UserErrorCode.USER_STATUS_ERROR);
        }
        this.status = newStatus;

        if (newStatus == AccountStatus.DISABLED) {
            this.leftAt = Instant.now();
        }

        addDomainEvent(
                new UserAccountStatusUpdated(
                        this.id, oldStatus.name(), this.status.name(), this.leftAt));
    }
}

// TODO : 비밀번호 해시화, email valid 검증, 비밀번호 valid 검증
