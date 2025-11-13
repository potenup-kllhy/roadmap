package com.kllhy.roadmap.common.exception;

import org.springframework.http.HttpStatus;

public final class HttpStatusMapper {
    private HttpStatusMapper() {}

    public static HttpStatus toSpringStatus(DomainHttpStatus s) {
        if (s == null) return HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            return HttpStatus.valueOf(s.getValue());
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
