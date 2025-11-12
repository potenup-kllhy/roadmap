package com.kllhy.roadmap.user.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum UserErrorCode implements IErrorCode {
    USER_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "USER_001", "not found user");

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    UserErrorCode(DomainHttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public DomainHttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
