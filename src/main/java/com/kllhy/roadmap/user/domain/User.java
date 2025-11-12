package com.kllhy.roadmap.user.domain;

import com.kllhy.roadmap.common.model.AggregateRoot;
import com.kllhy.roadmap.common.model.IdAuditEntity;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

@Entity
public class User extends AggregateRoot {
    @Getter
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, name = "password_hashed")
    private String password;

    @Getter
    @Column(nullable = false, name = "account_status")
    private AccountStatus status = AccountStatus.ACTIVE;

    @Getter private Instant leftAt;
}
