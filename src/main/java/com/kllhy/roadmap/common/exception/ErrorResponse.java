package com.kllhy.roadmap.common.exception;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        Instant timestamp, int status, String error, String code, String message, String path) {
    public static ErrorResponse of(HttpStatus http, IErrorCode code, String path) {
        return new ErrorResponse(
                Instant.now(),
                http.value(),
                http.getReasonPhrase(),
                code.getCode(),
                code.getMessage(),
                path);
    }
}
