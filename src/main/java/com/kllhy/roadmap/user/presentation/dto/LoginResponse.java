package com.kllhy.roadmap.user.presentation.dto;

import com.kllhy.roadmap.user.domain.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String loginId;
    private String email;
    private AccountStatus accountStatus;
}


