package com.ohgiraffers.loadmapuser.domain;

import lombok.*;

import java.time.LocalDateTime;

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