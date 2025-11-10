package com.kllhy.roadmap.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String loginId;
    private String email;
    private String password;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
