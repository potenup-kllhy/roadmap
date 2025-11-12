package com.kllhy.roadmap.user.application.command.dto;

import com.kllhy.roadmap.user.domain.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserCommand {
    private String loginId;
    private String email;
    private String password;
    private AccountStatus accountStatus;
}
