package com.kllhy.roadmap.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessCode implements IResponseCode{
    SUCCESS(HttpStatus.OK, "SUCCESS", "성공했습니다"),
    CREATED(HttpStatus.CREATED, "CREATED", "생성되었습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
