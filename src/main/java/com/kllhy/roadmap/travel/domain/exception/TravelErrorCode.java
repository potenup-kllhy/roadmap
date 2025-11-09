package com.kllhy.roadmap.travel.domain.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum TravelErrorCode implements IErrorCode {
    TRAVEL_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "TRAVEL_001", "not found travel"),
    ;

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    TravelErrorCode(DomainHttpStatus httpStatus, String code, String message) {
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
