package com.kllhy.roadmap.star.roadmap.presentation.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus code, String message) {}
