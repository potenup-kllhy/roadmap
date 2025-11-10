package com.kllhy.roadmap.user.domain.util;

import com.ohgiraffers.loadmapuser.domain.exception.InvalidPasswordException;

public class PasswordValidator {
    
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;
    
    public static void validate(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidPasswordException("Password cannot be null or empty.");
        }
        
        if (password.length() < MIN_LENGTH) {
            throw new InvalidPasswordException("Password must be at least " + MIN_LENGTH + " characters long.");
        }
        
        if (password.length() > MAX_LENGTH) {
            throw new InvalidPasswordException("Password must be at most " + MAX_LENGTH + " characters long.");
        }
    }
}

