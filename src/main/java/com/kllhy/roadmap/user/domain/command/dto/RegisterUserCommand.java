package com.kllhy.roadmap.user.domain.command.dto;

import com.ohgiraffers.loadmapuser.domain.AccountStatus;
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

