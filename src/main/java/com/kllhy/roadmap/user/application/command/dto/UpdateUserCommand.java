package com.kllhy.roadmap.user.application.command.dto;

import com.kllhy.roadmap.user.domain.model.enums.AccountStatus;
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
