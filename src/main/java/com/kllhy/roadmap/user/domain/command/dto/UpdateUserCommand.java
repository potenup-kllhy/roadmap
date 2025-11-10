package com.kllhy.roadmap.user.domain.command.dto;

import com.ohgiraffers.loadmapuser.domain.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand {
    private String email;
    private String password;
    private AccountStatus accountStatus;
}

