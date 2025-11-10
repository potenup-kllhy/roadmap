package com.kllhy.roadmap.user.domain.util;

import com.ohgiraffers.loadmapuser.domain.exception.InvalidEmailException;

import java.util.regex.Pattern;

public class EmailValidator {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    public static void validate(String email) {
        if (email == null || email.isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty.");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("Invalid email format.");
        }
    }
}

